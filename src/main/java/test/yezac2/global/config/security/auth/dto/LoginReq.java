package test.yezac2.global.config.security.auth.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginReq {

    private String username;    //email
    private String password;

}
