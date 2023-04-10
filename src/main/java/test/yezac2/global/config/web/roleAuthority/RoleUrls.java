package test.yezac2.global.config.web.roleAuthority;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RoleUrls {

    // 직군 상세 (roles 테이블)
    private String role;

    // 직군 상세별 접근 가능한 url (depth3_navigation_role, depth3_navigations 테이블)
    private List<String> urls_allowed;

}
