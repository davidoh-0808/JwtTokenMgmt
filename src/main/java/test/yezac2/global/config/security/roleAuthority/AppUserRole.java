package test.yezac2.global.config.security.roleAuthority;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum AppUserRole {
    
    // AppUserPermission 권한 과 AppUserRole 유저타입 매핑
    ADMIN( Sets.newHashSet(
            AppUserAuthority.USERS_READ, AppUserAuthority.USERS_WRITE,
            AppUserAuthority.RECORD_READ, AppUserAuthority.RECORD_WRITE,
            AppUserAuthority.ACCOUNT_READ, AppUserAuthority.ACCOUNT_WRITE
    ) ),
    DOCTOR( Sets.newHashSet(
            AppUserAuthority.RECORD_READ, AppUserAuthority.RECORD_WRITE
    ) ),
    NURSE( Sets.newHashSet(

    ) ),
    STAFF( Sets.newHashSet() ),
    MGMT( Sets.newHashSet(AppUserAuthority.ACCOUNT_READ, AppUserAuthority.ACCOUNT_WRITE) );

    private final Set<AppUserAuthority> authorities;

    AppUserRole(Set<AppUserAuthority> authorities) {
        this.authorities = authorities;
    }

    public Set<AppUserAuthority> getAuthorities() {
        return authorities;
    }


    /*
    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> authorities = getAuthorities().stream()
                .map( authority -> new SimpleGrantedAuthority(authority.getPermission()) )
                .collect(Collectors.toSet());

        authorities.add( new SimpleGrantedAuthority("ROLE_" + this.name()) );

        return authorities;
    }
    */

}
