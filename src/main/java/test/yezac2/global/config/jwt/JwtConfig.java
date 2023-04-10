package test.yezac2.global.config.jwt;

import com.google.common.net.HttpHeaders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


// application.yml 에서 값 가져옴
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;

    private int accessTokenExpirationInMinutes;
    private int accessTokenExpirationInHours;

    private int refreshTokenExpirationInHours;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

}
