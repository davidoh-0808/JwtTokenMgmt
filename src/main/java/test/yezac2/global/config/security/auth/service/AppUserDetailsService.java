package test.yezac2.global.config.security.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import test.yezac2.global.config.security.auth.domain.AppUserDetails;
import test.yezac2.global.config.security.auth.dto.JoinReq;
import test.yezac2.global.config.security.auth.dto.VerifTokenReq;
import test.yezac2.global.config.security.auth.mapper.AppUserMapper;
import test.yezac2.global.config.security.pw.PasswordUtil;
import test.yezac2.global.config.security.roleAuthority.AppUserRole;

import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserMapper appUserMapper;
    private final VerifTokenService verifTokenService;

    /**
     AuthenticationManager 가 .authenticate 로 유저를 인증 할 때 호출되는 함수
     유저인증시 체크할 JWT 이며, 이때 필요한 SimpleGrantedAuthority 들을 주입시켜 주어야한다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUserDetails> appUserDetails = appUserMapper.getAppUserByUsername(username);
        
        /* AppUserDetails 의 DB 컬럼 authType 체크
             -> authType 에 부합한 Enum Authorities 주입
         */
        if(appUserDetails.isPresent()) {
            AppUserDetails user = appUserDetails.get();
            String authType = user.getAuthType();
            EnumSet<AppUserRole> appUserRoles = EnumSet.allOf(AppUserRole.class);
            List<GrantedAuthority> authorities = new ArrayList<>();

            appUserRoles.forEach(
                appUserRole -> {
                    String role = appUserRole.name();
                    if( role.equals(authType) ) {
                        authorities.add(new SimpleGrantedAuthority( role ));
                    }
            });

            return new User( user.getUsername(), user.getPassword(), authorities );

        } else {

            return appUserDetails.orElseThrow( () ->
                    new UsernameNotFoundException(String.format("Username %s not found", username))
            );
        }
        
    }

    /**
     유저 존재여부 이메일로 체크 /
     유저(현재 비활성화) 생성 /
     이메일에 보낼 인증토큰 생성 및 프론트 단으로 리턴
     */
    public String joinAppUser(JoinReq req) {
        boolean userExists = appUserMapper.getAppUserByUsername( req.getEmail() )
                                .isPresent();
        if (userExists) {
            throw new IllegalStateException("입력받은 유저 이메일은 이미 사용중 입니다.");
        }

        // 유저(현재 비활성화) 생성
        String encodedPw = PasswordUtil.getPasswordEncoder()
                            .encode( req.getPassword() );
        req.setPassword(encodedPw);
        appUserMapper.saveAppUser(req);

        // 인증토큰 DB 생성 -> 이메일 용도 및 프론트엔드 에서 사용
        String tokenEmailAndFront = UUID.randomUUID().toString();
        VerifTokenReq verifTokenReq = VerifTokenReq.builder()
                                .token( tokenEmailAndFront )
                                .email( req.getEmail() )
                                .build();
        verifTokenService.saveVerifToken(verifTokenReq);

        return tokenEmailAndFront;
    }


    public int enableAppUser(String email) {
        return appUserMapper.enableAppUser(email);
    }


}
