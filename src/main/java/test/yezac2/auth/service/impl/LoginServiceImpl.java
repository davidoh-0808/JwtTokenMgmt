package test.yezac2.auth.service.impl;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import test.yezac2.auth.mapper.LoginMapper;
import test.yezac2.global.config.jwt.mapper.TokenMapper;
import test.yezac2.global.config.jwt.payload.AccessTokenIssueReq;
import test.yezac2.auth.payload.LoginReq;
import test.yezac2.auth.payload.LoginResp;
import test.yezac2.global.config.jwt.payload.RefreshTokenIssueReq;
import test.yezac2.auth.service.LoginService;
import test.yezac2.global.config.jwt.JwtConfig;
import test.yezac2.global.config.jwt.JwtSecretKey;
import test.yezac2.global.config.web.PasswordUtil;
import test.yezac2.global.config.web.roleAuthority.RoleUrls;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final LoginMapper loginMapper;
    private final TokenMapper tokenMapper;
    private final JwtConfig jwtConfig;
    private final JwtSecretKey jwtSecretKey;


    public LoginResp login(LoginReq loginReq) {
        // 일단 UUID 로 넣어놓음 (나중에 Jwts 로 생성해도 될듯 함)
        String refreshTokenNew = UUID.randomUUID().toString();      // TODO: insert this part into JwtConfig util class
        String accessToken = "";
        Date expirationAccess = null;
        Date expiredAtRefresh = null;

        // 로그인 resp 에 사용될 객체 생성
        LoginResp loginResp = loginMapper.login( loginReq );

        if( !loginResp.getEmail().isEmpty() ) {
            LoginResp userInfo = loginResp;
            String reqPw = loginReq.getPassword();
            PasswordEncoder encoder = PasswordUtil.getPasswordEncoder();

            List<GrantedAuthority> authorities = new ArrayList<>();

            // 입력된 pw 와 DB pw 가 같다면 -> access token 생성하여 리스폰스 리턴
            if( encoder.matches(reqPw, userInfo.getPassword()) ) {
                String userId = this.getUserIdByEmail( loginReq.getEmail() ).toString();

                // SELECT java_role FROM roles 테이블  // TODO: switch this to TokenMapper
                String java_role = loginMapper.getUserRole( Long.parseLong(userId) );

                // SELECT url_path FROM depth3_navigations 테이블  // TODO: switch this to TokenMapper
                List<String> urls_allowed = loginMapper.getUserUrlsAllowed( Long.parseLong(userId) );

                // 권한명, 권한 을 RoleUrls 객체로 래핑
                RoleUrls roleUrls = RoleUrls.builder()
                        .role(java_role)
                        .urls_allowed( urls_allowed )
                        .build();

                // TODO: insert this part into JwtConfig util class
                // access token 유효기간 변경은 여기서
                expirationAccess = java.sql.Timestamp.valueOf(
                    LocalDateTime.now().plusMinutes( Integer.toUnsignedLong(jwtConfig.getAccessTokenExpirationInMinutes()) )
                );

                // TODO: insert this part into JwtConfig util class
                // 로그인 resp 로 반환될 access_token 생성
                accessToken = Jwts.builder()
                        .setSubject( userId )
                        .claim("user_email", loginReq.getEmail() )
                        .claim( "role_urls", roleUrls )            // set role and allowed access urls in the claim
                        .setIssuedAt( new Date() )
                        .setExpiration( expirationAccess )
                        .signWith( jwtSecretKey.getSecretKey() )
                        .compact();

                // 로그인 resp 에 생성한 access_token 주입
                loginResp.setAccessToken( accessToken );

                long userIdL = Long.parseLong( userId );
                // 로그인 하는 유저의 refresh token 이 존재유무 & 유효기간 검증 (refresh token 이란 만료된 access token 을 재발급 하기 위한 토큰)
                boolean isRefreshTokenPresent = tokenMapper.isRefreshTokenPresent( userIdL );

                /* refresh token 존재유무 & 유효기간 검증이 되었다면
                   기존 refresh token 을 로그인 resp 에 담기
                   access token 생성 및 db 저장 */
                if( isRefreshTokenPresent ) {
                    String refreshToken = tokenMapper.getRefreshTokenPresent( userIdL );
                    loginResp.setRefreshToken( refreshToken );

                    // refresh token 이 기존에 없다면 새로 발급 / 로그인 resp 에 생성한 access_token 주입
                } else if ( !isRefreshTokenPresent ) {

                    // TODO: insert this part into JwtConfig util class
                    // refresh_token 유효기간 생성
                    expiredAtRefresh = java.sql.Timestamp.valueOf(
                        LocalDateTime.now().plusHours( jwtConfig.getRefreshTokenExpirationInHours() )
                    );

                    tokenMapper.issueRefreshToken(
                        RefreshTokenIssueReq.builder()
                            .userId( userIdL )
                            .refreshToken( refreshTokenNew )
                            .expiredAt( expiredAtRefresh )
                            .build()
                    );

                    // 로그인 resp 에 생성한 access_token 주입
                    loginResp.setRefreshToken( refreshTokenNew );

                // TODO: CHECK
                //  refresh token 이 만료됐다면, 만료된 refresh token 삭제 / 재발급 / 로그인 resp 에 담기
                } else {
                    boolean isRefreshTokenExpired = tokenMapper.isRefreshtokenExpiredByUserId( userIdL );
                    if (isRefreshTokenExpired) {
                        // TODO: insert this part into JwtConfig util class
                        // refresh_token 유효기간 생성
                        expiredAtRefresh = java.sql.Timestamp.valueOf(
                                LocalDateTime.now().plusDays( jwtConfig.getRefreshTokenExpirationInHours() )
                        );

                        tokenMapper.deleteRefreshTokenExpired(Long.parseLong( userId ));
                        tokenMapper.issueRefreshToken(
                                RefreshTokenIssueReq.builder()
                                        .userId( userIdL )
                                        .refreshToken( refreshTokenNew )
                                        .expiredAt( expiredAtRefresh )
                                        .build()
                        );
                    }

                    // 그리고 재발급한 토큰을 로그인 resp 에 담아 보낸다
                    loginResp.setRefreshToken( refreshTokenNew );
                }

                // TODO: insert this part into JwtConfig util class
                // refresh_token 유효기간 생성
                expiredAtRefresh = java.sql.Timestamp.valueOf(
                    LocalDateTime.now().plusMinutes( jwtConfig.getRefreshTokenExpirationInHours() )
                );

                // 위에서 생성한 refresh token 과 access token 을 db에 저장
                // TODO: ********* CHECK ********* db 에 access token 존재하면 지우고 다시 생성
                boolean isAccessTokenPresent = tokenMapper.isAccessTokenPresent( userIdL );
                if ( isAccessTokenPresent ) {
                    tokenMapper.deleteExistingAccessTokenByUserId( userIdL );
                }

                tokenMapper.issueAccessToken(
                    AccessTokenIssueReq.builder()
                        .userId(Long.parseLong( userId ))
                        .refreshToken( refreshTokenNew )
                        .accessToken( accessToken )
                        .expiredAt( expiredAtRefresh )
                        .build()
                );

                // 입력된 pw 와 DB pw 불일치
            } else {
                log.info( String.format("Password [%s] does not match", loginReq.getPassword()) );
                throw new RuntimeException(
                        String.format("Password [%s] does not match", loginReq.getPassword())
                );
            }

        // DB 에 해당 이메일의 유저 존재 무
        } else {
            log.info( String.format("No such user with email [%s] does not exist", loginReq.getEmail()) );
            throw new RuntimeException(
                    String.format("No such user with email [%s] does not exist", loginReq.getEmail())
            );
        }

        return loginResp;
    }


    @Override
    public Long getUserIdByEmail(String email) {
        return loginMapper.getUserIdByEmail(email);
    }


}
