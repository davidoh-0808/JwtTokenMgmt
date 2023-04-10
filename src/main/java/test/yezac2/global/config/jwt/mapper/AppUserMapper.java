package test.yezac2.global.config.jwt.mapper;

import org.apache.ibatis.annotations.Mapper;
import test.yezac2.global.config.jwt.model.AppUserDetails;
import test.yezac2.auth.payload.JoinReq;
import test.yezac2.global.config.jwt.payload.UserDetailsDto;

import java.util.Optional;

@Mapper
public interface AppUserMapper {

    Optional<AppUserDetails> getAppUserByUsername(String email);
    Optional<UserDetailsDto> getUserDetailsByEmail(String email);

    int saveAppUser(JoinReq req);

    int enableAppUser(String email);

}
