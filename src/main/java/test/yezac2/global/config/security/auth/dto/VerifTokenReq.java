package test.yezac2.global.config.security.auth.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VerifTokenReq {

    private String token;
    private String email;


    public VerifTokenReq(String token) {
        this.token = token;
    }

}
