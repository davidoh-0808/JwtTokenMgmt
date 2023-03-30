package test.yezac2.user.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

/*
    Spring Auth 를 위한 유저객체
 */
public class AppUserDetails implements UserDetails {

    // Spring Security 관련 필드
    private final String username;      // DB column - email
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;

    /*
    유저 권한 부여하기 위한 DB 컬럼 [ ADMIN , DOCTOR , NURSE , STAFF , MGMT , etc ]
    권한타입을 체크하여 로그인 시 위의 authorities 필드에 권한 주입시켜준다
    */
    // private String authType;

    /* 이 외 DB에 있는 컬럼
    private long id;
    private String name;
    private String email;
    private String firstRegistrationNumber;
    private String secondRegistrationNumber;
    private String phoneNumber;
    private String subPhoneNumber;
    private String address;
    private String medicalLicenseNumber;
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
     */

    //이메일을 통한 인증토큰 확인 여부
    // private boolean emailVerified;

    public AppUserDetails(String username,
                          String password,
                          Set<? extends GrantedAuthority> authorities,
                          boolean isAccountNonExpired,
                          boolean isAccountNonLocked,
                          boolean isCredentialsNonExpired,
                          boolean isEnabled) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }
    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}