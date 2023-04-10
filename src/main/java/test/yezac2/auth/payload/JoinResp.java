package test.yezac2.auth.payload;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JoinResp {

    private String verifToken; // 유저 가입 요청시 사용하는 verifToken

}
