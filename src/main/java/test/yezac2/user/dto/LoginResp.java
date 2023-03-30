package test.yezac2.user.dto;

import lombok.*;

import java.util.Collection;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
//@Schema(description = "유저 로그인 시 id+pw 를 통해 JWT 받기위한 모델 " +
//        "(JWT 토큰의 ROLE 이 액세스 권한이 있다면 response 를 보내준다)")
public class LoginResp {

    private long id;
    private String name;
    private String email;
    private String firstRegistrationNumber;
    private String secondRegistrationNumber;
    private String phoneNumber;
    private String subPhoneNumber;
    private String address;
    private String medicalLicenseNumber;
    private String password;
    private Date joinedAt;
    private Date leavedAt;
    private Date expiredDate;
    private long hospitalId;
    private long roleId;
    private int isLocked;
    private int isDeleted;
    private String doctorLicenseNumber;
    private Date createdAt;
    private Date updatedAt;

    private String jwt;


}
