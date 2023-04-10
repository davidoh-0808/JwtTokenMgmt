package test.yezac2.global.config.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;
import test.yezac2.global.config.jwt.exception.AccessTokenExpiredException;
import test.yezac2.global.config.web.roleAuthority.RoleUrls;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/*
    request 가 들어올 때 마다 헤더의 JWT 토큰을 파싱하여
    username / authorities (권한) 을 꺼내고 권한으로 SimpleGrantedAuthorities 를 만듬

    ****** 현재 request 에 담긴 url 주소가 jwt의 urls_allowed 에 있지 않다면 sendError(401 status) (UsernamePasswordAuthenticationFilter 에서 주입됌) *****

    username 과 authorities 가 담긴 authentication 객체 생성하여 실어주기

    OncePerRequestFilter :
       makes a single execution for each request to our API.
       It provides a doFilterInternal() method that we will implement parsing & validating JWT
       , loading User details (using UserDetailsService)
       , checking Authorizaion (using UsernamePasswordAuthenticationToken)
 */
@RequiredArgsConstructor
@Configuration
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final JwtSecretKey jwtSecretKey;
    // private final AccessTokenExpiredHandler tokenExpiredHandler;
    private static final String[] excludedUrls = {
            "/auth"
            , "/auth/refreshAccessToken"
            , "/authenticate"
            , "/swagger-ui/index.html"
            , "/swagger-resources/**"
            , "/v3/api-docs/**"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest servletReq) throws ServletException {
        String url = servletReq.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        return Stream.of( excludedUrls ).anyMatch(
                x -> pathMatcher.match(x, url)
               );
    }

    @Override
    @ExceptionHandler({
            AccessTokenExpiredException.class
    })
    protected void doFilterInternal(HttpServletRequest servletReq, HttpServletResponse servletResp, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = servletReq.getHeader(jwtConfig.getAuthorizationHeader());


        // 리퀘스트 헤더에 JWT 인증 헤더가 없다면 403 error 발생 및 해당 필터종료
        if (Strings.isNullOrEmpty(authHeader) || !authHeader.startsWith(jwtConfig.getTokenPrefix())) {
            // servletResp.sendError(HttpServletResponse.SC_FORBIDDEN, "리퀘스트 헤더에 JWT token 이 담겨있지 않습니다");
            filterChain.doFilter(servletReq, servletResp);

            return;
        }

        // JWT 토큰 있다면 파싱하여 유저의 Permission 과 연동
        String jwtToParse = authHeader.replace( jwtConfig.getTokenPrefix(), "").trim();
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey( jwtSecretKey.getSecretKey() )
                    .build()
                    .parseClaimsJws( jwtToParse );

            Claims body = claimsJws.getBody();

            // JWT 유효기간 체크하고 유효기간 지났을 시, 401 UNAUTHORIZED ERROR 발생
            Date tokenExpiresAt = body.getExpiration();
            if( tokenExpiresAt.before( Calendar.getInstance().getTime() ) ) {
                String errMsg = String.format("JWT 유효기간이 지났습니다 JWT expiration date : [%s]", tokenExpiresAt);
                servletResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, errMsg);
            }

            // JWT subject 에서 유저 고유번호(id) 가져옴
            String username = body.getSubject();

            // JWT body의 "authorities"에 주입된 권한 가져와서 실제 유저의 GrantedAuthorities 만들어 Authentication에 주입
            LinkedHashMap<String, Object> role_urls = (LinkedHashMap) body.get("role_urls");
            String role = (String) role_urls.remove( "role" );
            List<String> urls_allowed = (List<String>) role_urls.remove( "urls_allowed" );

            RoleUrls roleUrls = RoleUrls.builder()
                                    .role( role )
                                    .urls_allowed( urls_allowed )
                                    .build();

            // 현재 request 에 담긴 url 주소가 jwt 의 urls_allowed 에 있지 않다면,
            // sendError(403 status) (UsernamePasswordAuthenticationFilter 에서 주입되어 옴) *****
            // if the user's jwt token contains "schedule/hospital/daily", any requests like these, "schedule/hospital/daily/*", passes
            String currentUrl = servletReq.getRequestURI();

            boolean isCurrentUrlAllowed = true;
            for(String url : urls_allowed) {
                if(currentUrl.lastIndexOf( url ) == -1 ) {
                    isCurrentUrlAllowed = false;
                } else {
                    break;
                }
            }

            if( !isCurrentUrlAllowed ) {
                String msg = String.format("유저는 현재 요청하는 API URI[%s] 에 대한 권한이 없습니다.", servletReq.getRequestURI());
                logger.info( msg );
                servletResp.sendError(HttpServletResponse.SC_FORBIDDEN, msg);

                return;
            }

            // Spring Security Context 에 유저 인증용 Spring Security Model 입력
            List<SimpleGrantedAuthority> simpleGrantedAuths = new ArrayList<>();
            for(String url : roleUrls.getUrls_allowed()) {
                simpleGrantedAuths.add( new SimpleGrantedAuthority(url) );
            }
            Authentication auth = new UsernamePasswordAuthenticationToken( /* username : user_id */ username, null, simpleGrantedAuths );
            SecurityContextHolder.getContext().setAuthentication( auth );

        // 요청 시 헤더에 입력한 access token 이 만료되었다면, 다음 에러로 캐치하여 처리
        } catch (SignatureException e) {
            logger.info( e.getMessage() );
            //String msg = "SignatureException : access token 에 문제가 있습니다";
            //servletResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);
            servletResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;

        } catch (MalformedJwtException e) {
            logger.info( e.getMessage() );
            //String msg = "MalformedJwtException : access token 에 문제가 있습니다";
            //servletResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);
            servletResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;

        } catch (ExpiredJwtException e) {
            logger.info( e.getMessage() );

            // 프론트 단에서 401 UNAUTHORIZED 상태값과 어떻게 처리할지 안내 메시지를 보내준다
            String guideMsg = "요청 시 헤더에 입력한 access token 이 만료되었습니다." +
                    "보관하고 있는 refresh token 을 사용하여 /auth/refreshAccessToken 을 호출하면 access token 을 재발급 받을 수 있습니다.";

            // servletResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);
            /* 이게 제대로 작동 안함 (프론트에서 401 에러와 메시지 못 받음 )
                **** 테스트 결과 알아낸 사실 **** :
                SecurityFilterChain 은 @ControllerAdvice, @ExceptionHandler 와 같은 컴포넌트 들 보다 우선적으로 발동하기 때문에
                커스텀 Exception 을 띄워 리스폰스 처리하는 것은 무리였다

                //throw new AccessTokenExpiredException( msg );
                //AccessTokenExpiredHandler.handleAccessTokenExpiration( new AccessTokenExpiredException( guideMsg ), servletReq );
                //return;
            */
            servletResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;


        } catch (UnsupportedJwtException e) {
            logger.info( e.getMessage() );
            //String msg = "UnsupportedJwtException : access token 에 문제가 있습니다";
            //servletResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);
            servletResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;

        } catch (IllegalArgumentException e) {
            logger.info( e.getMessage() );
            //String msg = "IllegalArgumentException : access token 에 문제가 있습니다";
            //servletResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);
            servletResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;
        }


        // 해당 필터 종료하고 다음 필터로 넘김
        filterChain.doFilter(servletReq, servletResp);
    }

}
