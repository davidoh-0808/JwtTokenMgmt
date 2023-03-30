package test.yezac2.user.service.impl;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import test.yezac2.user.dto.LoginReq;
import test.yezac2.user.dto.LoginResp;
import test.yezac2.user.mapper.LoginMapper;
import test.yezac2.user.service.LoginService;
import test.yezac2.global.config.security.jwt.JwtConfig;
import test.yezac2.global.config.security.jwt.JwtSecretKey;
import test.yezac2.global.config.security.pw.PasswordUtil;
import test.yezac2.global.config.security.roleAuthority.RoleUrls;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final LoginMapper loginMapper;
    private final JwtConfig jwtConfig;
    private final JwtSecretKey jwtSecretKey;


    public LoginResp login(LoginReq loginReq) {

        LoginResp loginResp = loginMapper.login(loginReq);
        if( !loginResp.getEmail().isEmpty() ) {
            LoginResp userInfo = loginResp;

            String reqPw = loginReq.getPassword();
            PasswordEncoder encoder = PasswordUtil.getPasswordEncoder();

            // ERASE & FIX THIS PART
            //String authType = loginResp.getAuthType();
            //EnumSet<AppUserRole> appUserRoles = EnumSet.allOf(AppUserRole.class);


            List<GrantedAuthority> authorities = new ArrayList<>();
            /*
            appUserRoles.forEach(
                appUserRole -> {
                    String role = appUserRole.name();
                    if( role.equals(authType) ) {
                        // 1st forEach - JWT 에 역할 (java_role) 부여
                        authorities.add(new SimpleGrantedAuthority( "ROLE_" + role ));

                        appUserRole.getAuthorities().forEach(
                                authority -> {
                                    // 2nd forEach - JWT 에 권한들 () 부여
                                    authorities.add(new SimpleGrantedAuthority( authority.name() ));
                                }
                        );
                    }
                }
            );
             */

            // 입력된 pw 와 DB pw 가 같다면 -> JWT 생성하여 리스폰스 리턴
            // if( encoder.matches(reqPw, userInfo.getPassword()) ) {
            if (true) {
                String userId = this.getUserIdByEmail( loginReq.getEmail() ).toString();

                // CHECK -> FETCH java_role FROM roles 테이블
                String java_role = loginMapper.getUserRole( Long.parseLong(userId) );

                // CHECK -> FETCH url_path FROM depth3_navigations 테이블
                List<String> urls_allowed = loginMapper.getUserUrlsAllowed( Long.parseLong(userId) );

                // CHECK -> wrap the role and access urls into RoleUrls object
                RoleUrls roleUrls = RoleUrls.builder()
                                        .role(java_role)
                                        .urls_allowed(urls_allowed)
                                        .build();

                String jwtIssued = Jwts.builder()
                        .setSubject( userId )
                        .claim("user_email", loginReq.getEmail() )
                        .claim( "role_urls", roleUrls )            // set role and allowed access urls in the claim
                        .setIssuedAt(new Date())
                        .setExpiration(java.sql.Date.valueOf(
                                LocalDateTime.now()
                                    .plusHours( Integer.toUnsignedLong(jwtConfig.getTokenExpirationAfterHours()) )
                                    .toLocalDate()
                        ))
                        .signWith( jwtSecretKey.getSecretKey() )
                        .compact();

                loginResp.setJwt( jwtIssued );

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
