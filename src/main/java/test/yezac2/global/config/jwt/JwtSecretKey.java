package test.yezac2.global.config.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Configuration
public class JwtSecretKey {

    private final JwtConfig jwtConfig;

    @Bean
    public SecretKey getSecretKey() {
        byte[] secretKeyToByte = jwtConfig.getSecretKey().getBytes();

        return Keys.hmacShaKeyFor( secretKeyToByte );
    }

}
