package test.yezac2.global.config.security.auth.domain;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VerifToken {

    private long id;
    private String token;
    private Date createdAt;
    private Date expiresAt;
    private Date verifiedAt;
    private long usersId;
    private String usersEmail;       // 인증요청을 하는 유저의 이메일 (현재 가져오는 코드 missing?)

}
