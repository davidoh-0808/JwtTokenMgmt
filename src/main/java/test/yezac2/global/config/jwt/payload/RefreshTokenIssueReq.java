package test.yezac2.global.config.jwt.payload;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RefreshTokenIssueReq {

    private long userId;
    private String refreshToken;
    private Date expiredAt;

}
