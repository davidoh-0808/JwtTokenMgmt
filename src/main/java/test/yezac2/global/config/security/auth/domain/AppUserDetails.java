package test.yezac2.global.config.security.auth.domain;

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
    private String authType;

    // 이 외 DB에 있는 컬럼
    private long id;
    private String email;
    private String name;
    private String residentResi;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Date expiredDate;
    private long hospitalId;

    //이메일을 통한 인증토큰 확인 여부
    private boolean emailVerified;


    public AppUserDetails(String name,
                          String password,
                          Set<? extends GrantedAuthority> authorities,
                          boolean isAccountNonExpired,
                          boolean isAccountNonLocked,
                          boolean isCredentialsNonExpired,
                          boolean isEnabled) {
        this.username = name;
        this.password = password;
        this.authorities = authorities;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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


    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResidentResi() {
        return residentResi;
    }

    public void setResidentResi(String residentResi) {
        this.residentResi = residentResi;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }


}