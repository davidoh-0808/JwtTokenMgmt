package test.yezac2.global.config.security.auth.service.impl;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import test.yezac2.global.config.security.auth.dto.LoginReq;
import test.yezac2.global.config.security.auth.dto.LoginResp;
import test.yezac2.global.config.security.auth.mapper.LoginMapper;
import test.yezac2.global.config.security.auth.service.LoginService;
import test.yezac2.global.config.security.jwt.JwtConfig;
import test.yezac2.global.config.security.jwt.JwtSecretKey;
import test.yezac2.global.config.security.pw.PasswordUtil;
import test.yezac2.global.config.security.roleAuthority.AppUserRole;

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
        if( !loginResp.getEmail().isEmpty() && !loginResp.getAuthType().isEmpty() ) {
            LoginResp userInfo = loginResp;

            String reqPw = loginReq.getPassword();
            PasswordEncoder encoder = PasswordUtil.getPasswordEncoder();

            // TODO: ERASE & FIX THIS PART
            //  ( replace Role & Authority
            //  w/ java_role(roles 테이블) & url_path (depth3_navigations 테이블) )
            String authType = loginResp.getAuthType();
            //EnumSet<AppUserRole> appUserRoles = EnumSet.allOf(AppUserRole.class);
            List<GrantedAuthority> authorities = new ArrayList<>();
            /*
            appUserRoles.forEach(
                appUserRole -> {
                    String role = appUserRole.name();
                    if( role.equals(authType) ) {
                        // 1st forEach - JWT 에 역할 (java_role) 부여
                        authorities.add(new SimpleGrantedAuthority( "ROLE_" + role ));              // TODO

                        appUserRole.getAuthorities().forEach(
                                authority -> {
                                    // 2nd forEach - JWT 에 권한들 () 부여
                                    authorities.add(new SimpleGrantedAuthority( authority.name() ));     // TODO
                                }
                        );
                    }
                }
            );
             */

            // 입력된 pw 와 DB pw 가 같다면 -> JWT 생성하여 리스폰스 리턴
            // if( encoder.matches(reqPw, userInfo.getPassword()) ) {
            if (true) {
                String userId = this.getUserIdByEmail( loginReq.getUsername() ).toString();

                // TODO: FETCH java_role FROM roles 테이블
                String java_role = "";   // <<< query here

                // TODO: FETCH url_path FROM depth3_navigations 테이블
                List<String> urls_allowed = new ArrayList<>();     //  <<< query here

                String jwtIssued = Jwts.builder()
                        .setSubject( userId )
                        .claim( "java_role", java_role )            // TODO: set urls_allowed in the claim
                        .claim( "urls_allowed", urls_allowed )            // TODO: set urls_allowed in the claim
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
            log.info( String.format("No such user with email [%s] does not exist", loginReq.getUsername()) );
            throw new RuntimeException(
                    String.format("No such user with email [%s] does not exist", loginReq.getUsername())
            );
        }

        return loginResp;
    }

    @Override
    public Long getUserIdByEmail(String email) {
        return loginMapper.getUserIdByEmail(email);
    }


}
