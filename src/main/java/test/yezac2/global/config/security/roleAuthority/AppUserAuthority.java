package test.yezac2.global.config.security.roleAuthority;

public enum AppUserAuthority {

    // ADMIN 권한 예시
    USERS_READ("users:read"),
    USERS_WRITE("users:write"),
    // DOCTOR 권한 예시
    RECORD_READ("record:read"),
    RECORD_WRITE("record:write"),
    // NURSE 권한 예시

    // Staff 권한 예시
    
    // Mgmt 권한 예시
    ACCOUNT_READ("account:read"),
    ACCOUNT_WRITE("account:write");


    private final String permission;

    AppUserAuthority(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
