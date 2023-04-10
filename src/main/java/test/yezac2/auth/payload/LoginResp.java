package test.yezac2.auth.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(
        value = {
                "firstRegistrationNumber",
                "secondRegistrationNumber",
                "phoneNumber",
                "subPhoneNumber",
                "address",
                "medicalLicenseNumber",
                "password",
                "joinedAt",
                "leavedAt",
                "expiredDate",
                "hospitalId",
                "roleId",
                "doctorLicenseNumber",
                "createdAt",
                "updatedAt"
        }
)
//@Schema(description = "유저 로그인 시 id+pw 를 통해 JWT 받기위한 모델"
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


    private String refreshToken;
    private String accessToken;

}



