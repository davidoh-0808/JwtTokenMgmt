package test.yezac2.auth.payload;

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
