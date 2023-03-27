package test.yezac2.global.config.security.jwt;

import com.google.common.net.HttpHeaders;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// application.yml 에서 값 가져옴
@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    //    private Integer tokenExpirationAfterDays;
    private Integer tokenExpirationAfterHours;

    public JwtConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

//    public Integer getTokenExpirationAfterDays() {
//        return tokenExpirationAfterDays;
//    }
//    public void setTokenExpirationAfterDays(Integer tokenExpirationAfterDays) {
//        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
//    }

    public Integer getTokenExpirationAfterHours() {
        return tokenExpirationAfterHours;
    }

    public void setTokenExpirationAfterHours(Integer tokenExpirationAfterHours) {
        this.tokenExpirationAfterHours = tokenExpirationAfterHours;
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
