package test.yezac2.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginReq {

    private String email;    //email
    private String password;

}
