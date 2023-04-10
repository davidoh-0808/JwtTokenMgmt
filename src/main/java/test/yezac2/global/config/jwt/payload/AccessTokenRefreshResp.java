package test.yezac2.global.config.jwt.payload;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AccessTokenRefreshResp {

    private String refreshToken;        // 만료된 access 토큰 재발급에 사용되는 토큰
    private String newAccessToken;         // 로그인 시 발급받고 매 요청 시 사용되는 토큰

}
