package test.yezac2.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JoinReq {
    private String email;
    private String password;
    private String name;
    private String authType;
    private long hospitalId;
}
