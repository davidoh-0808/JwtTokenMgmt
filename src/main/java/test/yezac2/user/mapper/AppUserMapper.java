package test.yezac2.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import test.yezac2.user.domain.AppUserDetails;
import test.yezac2.user.dto.JoinReq;
import test.yezac2.user.dto.UserDetailsDto;

import java.util.Optional;

@Mapper
public interface AppUserMapper {

    Optional<AppUserDetails> getAppUserByUsername(String email);
    Optional<UserDetailsDto> getUserDetailsByEmail(String email);

    int saveAppUser(JoinReq req);

    int enableAppUser(String email);

}
