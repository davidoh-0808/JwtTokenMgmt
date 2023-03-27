package test.yezac2.global.config.security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    protected void doFilterInternal(HttpServletRequest servletReq, HttpServletResponse servletResp, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = servletReq.getHeader(jwtConfig.getAuthorizationHeader());

        // 리퀘스트 헤더에 JWT 인증 헤더가 없다면 해당 필터종료
        if (Strings.isNullOrEmpty(authHeader) || !authHeader.startsWith(jwtConfig.getTokenPrefix())) {
            servletResp.sendError(HttpServletResponse.SC_FORBIDDEN, "리퀘스트 헤더에 JWT token 이 담겨있지 않습니다");

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

            // TODO: CHECK if username is id  OR  email
            // JWT subject 에서 유저 고유번호(id) 가져옴
            String username = body.getSubject();

            // JWT body의 "authorities"에 주입된 권한 가져와서 실제 유저의 GrantedAuthorities 만들어 Authentication에 주입
            // TODO:
            //  1) extract the current request url from HTTPServletRequest
            //  2) extract the urls allowed from the claim body
            //  3-1) compare 1) with 2),
            //       if not exist -> sendError(HttpServletResponse.SC_FORBIDDEN, "현재 접속하는 URL에 대한 권한이 없습니다 (jwt 토큰의 urls_allowed 에 현재 접근 URL이 없음)")
            //  3-2) inject it into Authentication object


            // TODO: ****** 현재 request 에 담긴 url 주소가 jwt 의 urls_allowed 에 있지 않다면, sendError(401 status) (UsernamePasswordAuthenticationFilter 에서 주입됌) *****
            String currentUrl = servletReq.getRequestURL().toString();  // servletReq.getQueryString()
            if(currentUrl.equals( "" )) {

            }

            //List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
            List<Map<String, String>> roleUrls = (List<Map<String, String>>) body.get("roleUrls");

            // TODO: A LITTLE IFFY
            Set<SimpleGrantedAuthority> simpleGrantedAuth = roleUrls.stream()
                                                        .map( m -> new SimpleGrantedAuthority( m.get("urls_allowed") ) )
                                                        .collect(Collectors.toSet());

            Authentication auth = new UsernamePasswordAuthenticationToken(
                                            username,
                                            null,
                                            simpleGrantedAuth
                                    );

            SecurityContextHolder.getContext().setAuthentication( auth );

        } catch (JwtException e) {
            throw new IllegalStateException(String.format("요청 시 받은 JWT[%s] 객체에 이상이 있습니다.", jwtToParse));
        }

        // 해당 필터 종료하고 다음 필터로 넘김
        filterChain.doFilter(servletReq, servletResp);
    }

}
