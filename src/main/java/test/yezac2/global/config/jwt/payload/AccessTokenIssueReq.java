package test.yezac2.global.config.jwt.payload;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
//@Schema()
public class AccessTokenIssueReq {

    private long userId;
    private String refreshToken;    // access token 재발급에 사용되는 설정용 토큰
    private String accessToken;     // 로그인 시 발급 받아 모든 요청시 사용되는 토큰
    private Date expiredAt;

}
