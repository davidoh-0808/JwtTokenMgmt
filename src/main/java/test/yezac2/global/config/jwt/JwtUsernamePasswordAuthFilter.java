package test.yezac2.global.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import test.yezac2.auth.service.LoginService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

/* 로그인 사용되는 호출되는 필터

   UsernamePasswordAuthenticationFilter :
         gets {username, password} from login Request
         , AuthenticationManager will use it to authenticate a login account
 */
@RequiredArgsConstructor
public class JwtUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtConfig jwtConfig;
    private final JwtSecretKey jwtSecretKey;            // JwtConfig -> JwtSecretKey (키생성) -> WebSecurityConfig (전달)

    // AuthenticationManager : returns a fully populated Authentication object (including granted authorities)
    private final AuthenticationManager authenticationManager;

    private final LoginService loginService;


    // 로그인 request 에 담긴 username, password가 실제 DB 유저와 동일한지 체크
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            UsernamePasswordAuthReq userPassAuthReq =
                    new ObjectMapper().readValue( request.getInputStream(), UsernamePasswordAuthReq.class);

            Authentication usernamePasswordToken =
                    new UsernamePasswordAuthenticationToken(
                            userPassAuthReq.getUsername(),
                            userPassAuthReq.getPassword()
                    );

            // AuthenticationManager : returns a fully populated Authentication object (including granted authorities)
            Authentication authComplete = authenticationManager.authenticate( usernamePasswordToken );
            authComplete.isAuthenticated();

            return authComplete;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // 인증 성공 후 -> authentication 객체의 username(id OR email) 과 urls_allowed(권한들)을 꺼내어 JWT 생성 -> response 헤더에 주입
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // RoleUrls object (create Role)
        Long userId = loginService.getUserIdByEmail( authResult.getName() );

        String accessTokenIssued = Jwts.builder()
                        // 인증 성공 시, authentication 객체에 담긴 유저의 username(email) 을 subject로 설정
                        // if username is id  OR  email
                        .setSubject( authResult.getName() )
                        .claim("roleUrls", authResult.getAuthorities())

                        // insert the user's id 유저고유번호 as a claim (query -> getUserIdByEmail)
                        .claim("userId", userId)
                        .setIssuedAt(new Date())

                        // TODO: insert this part into JwtConfig util class
                        .setExpiration(java.sql.Timestamp.valueOf(
                                LocalDateTime.now()
                                .plusMinutes( Integer.toUnsignedLong(jwtConfig.getAccessTokenExpirationInMinutes()) )
                        ))
                        .signWith( jwtSecretKey.getSecretKey() )
                        .compact();

        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + accessTokenIssued);
    }


}
