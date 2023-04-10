package test.yezac2.global.config.jwt.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties(value = {
        "newAccessToken"
        , "userId"
        , "expiredAt"
})
public class AccessTokenRefreshReq {

    private String refreshToken;    // 만료된 access token 재발급 하기위해 로그인 시 리턴받은 refresh token 을 사용한다


    private String newAccessToken;
    private long userId;
    private Date expiredAt;

}
