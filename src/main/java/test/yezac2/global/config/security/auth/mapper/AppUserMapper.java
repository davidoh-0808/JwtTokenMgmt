package test.yezac2.global.config.security.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import test.yezac2.global.config.security.auth.domain.AppUserDetails;
import test.yezac2.global.config.security.auth.dto.JoinReq;

import java.util.Optional;

@Mapper
public interface AppUserMapper {

    Optional<AppUserDetails> getAppUserByUsername(String username);

    int saveAppUser(JoinReq req);

    int enableAppUser(String email);

}
