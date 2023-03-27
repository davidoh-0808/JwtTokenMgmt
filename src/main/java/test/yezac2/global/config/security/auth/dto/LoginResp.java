package test.yezac2.global.config.security.auth.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
//@Schema(description = "유저 로그인 시 id+pw 를 통해 JWT 받기위한 모델 " +
//        "(JWT 토큰의 ROLE 이 액세스 권한이 있다면 response 를 보내준다)")
public class LoginResp {

    private long id;
    private String email;
    private String password;
    private String authType;
    private long hospitalId;

    private String jwt;

}
