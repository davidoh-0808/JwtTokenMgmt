package test.yezac2.global.config.jwt.service;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.yezac2.auth.mapper.LoginMapper;
import test.yezac2.global.config.jwt.JwtConfig;
import test.yezac2.global.config.jwt.JwtSecretKey;
import test.yezac2.global.config.jwt.exception.RefreshAccessTokenException;
import test.yezac2.global.config.jwt.mapper.TokenMapper;
import test.yezac2.global.config.jwt.payload.AccessTokenRefreshReq;
import test.yezac2.global.config.jwt.payload.AccessTokenRefreshResp;
import test.yezac2.global.config.web.roleAuthority.RoleUrls;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final LoginMapper loginMapper;
    private final TokenMapper tokenMapper;
    private final JwtConfig jwtConfig;
    private final JwtSecretKey jwtSecretKey;


    /**
     *    Access Token 재발급 대략적인 순서
     *     1. 유저 refresh token 존재유무 확인
     *     2. 유저 refresh token 만료유무 확인
     *     3. 유저 access token 기존 것에서 유저정보 추출 (java_role, urls_allowed)
     *     4. 유저 access token 기존 것 삭제
     *     5. 유저 access token 새로 생성
     *     6. 유저 access token 새로 발급 (db 저장 , 리턴)
    */
    @Transactional
    public AccessTokenRefreshResp refreshAccessToken(AccessTokenRefreshReq req) {

        /*
            AccessTokenRefreshReq :
                String refreshToken
                String newAccessToken
                long userId
                Date expiredAt
         */

        /*
            AccessTokenRefreshResp :
                String refreshToken
                String newAccessToken
         */

        long userId = tokenMapper.getUserIdFromRefreshToken( req.getRefreshToken() );
        boolean isRefreshExp = tokenMapper.isRefreshtokenExpiredByUserId( userId );
        if( isRefreshExp ) {
            throw new RefreshAccessTokenException("401 FORBIDDEN 상태 원인: Refresh 토큰 만료  |  상태 처리에 필요한 액션: Refresh 토큰 재발급");
        }


        // TODO: switch this to TokenMapper
        String java_role = loginMapper.getUserRole( userId );
        List<String> urls_allowed = loginMapper.getUserUrlsAllowed( userId );
        RoleUrls roleUrls = RoleUrls.builder()
                .role(java_role)
                .urls_allowed( urls_allowed )
                .build();


        tokenMapper.deleteExistingAccessToken( req.getRefreshToken() );


        // TODO: insert this part into JwtConfig util class
        // access token 유효기간 변경은 여기서
        Date expirationAccess = java.sql.Timestamp.valueOf(
                LocalDateTime.now().plusMinutes( Integer.toUnsignedLong(jwtConfig.getAccessTokenExpirationInMinutes()) )
        );
        // 로그인 resp 로 반환될 access_token 생성
        String newAccessToken = Jwts.builder()
                .setSubject(String.valueOf( userId ))
                .claim("user_email", loginMapper.getEmailByUserId( userId ) )
                .claim( "role_urls", roleUrls )            // set role and allowed access urls in the claim
                .setIssuedAt( new Date() )
                .setExpiration( expirationAccess )
                .signWith( jwtSecretKey.getSecretKey() )
                .compact();

        req.setNewAccessToken(req.getNewAccessToken() );
        req.setUserId( userId );
        req.setExpiredAt( expirationAccess );
        tokenMapper.reissueAccessToken( req );


        return AccessTokenRefreshResp.builder()
                .refreshToken( req.getRefreshToken() )
                .newAccessToken( newAccessToken )
                .build();

    }


}
