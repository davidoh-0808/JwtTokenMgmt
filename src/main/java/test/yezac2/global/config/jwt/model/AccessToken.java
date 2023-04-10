package test.yezac2.global.config.jwt.model;

import lombok.*;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AccessToken {

    private long id;
    private long userId;
    private String token;
    private Date createdAt;
    private Date updatedAt;
    private Date expiredAt;
    private boolean revoked;
    private String name;

}
