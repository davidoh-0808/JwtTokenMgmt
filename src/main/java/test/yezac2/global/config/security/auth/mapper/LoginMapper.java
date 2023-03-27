package test.yezac2.global.config.security.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import test.yezac2.global.config.security.auth.dto.LoginReq;
import test.yezac2.global.config.security.auth.dto.LoginResp;

@Mapper
public interface LoginMapper {

    LoginResp login(LoginReq loginReq);

    Long getUserIdByEmail(String email);

}
