package test.yezac2.global.config.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import test.yezac2.auth.service.VerifTokenService;
import test.yezac2.global.config.jwt.mapper.AppUserMapper;
import test.yezac2.global.config.jwt.payload.UserDetailsDto;
import test.yezac2.auth.payload.JoinReq;
import test.yezac2.auth.payload.VerifTokenReq;
import test.yezac2.global.config.web.PasswordUtil;

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
        String email = username;
        Optional<UserDetailsDto> userDetailsDto = appUserMapper.getUserDetailsByEmail( email );

        if( userDetailsDto.isPresent() ) {
            List<GrantedAuthority> authorities = new ArrayList<>();

            return new User( userDetailsDto.get().getEmail(), userDetailsDto.get().getPassword(), authorities );
        } else {
            log.info( String.format("이메일([%s]) 를 가진 유저는 존재하지 않습니다.", email) );
            return null;
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
