package test.yezac2.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import test.yezac2.global.config.security.auth.service.AppUserDetailsService;
import test.yezac2.global.config.security.jwt.JwtConfig;
import test.yezac2.global.config.security.jwt.JwtSecretKey;
import test.yezac2.global.config.security.jwt.JwtTokenVerifier;
import test.yezac2.global.config.security.jwt.JwtUsernamePasswordAuthFilter;
import test.yezac2.global.config.security.pw.PasswordUtil;
import test.yezac2.global.config.security.roleAuthority.AppUserRole;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final AppUserDetailsService appUserDetailsService;
    private final JwtSecretKey jwtSecretKey;
    private final JwtConfig jwtConfig;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private static final String[] AUTH_WHITELIST = {
            "/login"
            , "/user/login"
            , "/user/join/**"
            , "/api/oauth2/**"
            , "/authenticate"
            , "/swagger-resources/**"
            , "/swagger-ui/**"
            , "/swagger-ui.html"
            , "/v3/api-docs"
            , "/webjars/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS )

        .and()

            // JWT 관련 필터 설정 (유저정보인증 (로그인 용도) 필터 -> 그 다음 JWT 인증절차에서 유저 권한 Authentication 객체에 주입 )
            .addFilter(new JwtUsernamePasswordAuthFilter( jwtConfig,
                                                        jwtSecretKey,
                                                        authenticationManagerBuilder.getObject() ))
            .addFilterAfter( new JwtTokenVerifier(jwtConfig, jwtSecretKey), JwtUsernamePasswordAuthFilter.class )

            /* http request header에 담긴 jwt의 username(id)를 통해 db 유저를 가져옴.
               또한 이 유저의 패스워드가 jwt 의 패스워드와 동일한지 확인. */
            .authenticationProvider( daoAuthenticationProvider() )

            /* 진입점을 제외한 모든 엔드포인트들은 유저권한(어드민, 의사, 간호사..) 로 분리
               hasRole 대신 hasAuthority( enum ) - 유저의 권한별 - 로도 분리 가능 */
            .authorizeHttpRequests(requests -> requests
                .requestMatchers( AUTH_WHITELIST ).permitAll()
                //.requestMatchers("/*").hasRole( AppUserRole.ADMIN.name() )
                //.requestMatchers("/*/doctor/*").hasRole( AppUserRole.DOCTOR.name() )
                //.requestMatchers("/*/nurse/*").hasRole( AppUserRole.NURSE.name() )
                //.requestMatchers("/*/staff/*").hasRole( AppUserRole.STAFF.name() )
                //.requestMatchers("/*/mgmt/*").hasRole( AppUserRole.MGMT.name() )

                // 모든 url 은 인증 절차 완료 되어야 됌
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder( PasswordUtil.getPasswordEncoder() );
        provider.setUserDetailsService( appUserDetailsService );

        return provider;
    }


}

