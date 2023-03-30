package test.yezac2.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import test.yezac2.user.dto.LoginReq;
import test.yezac2.user.dto.LoginResp;

import java.util.List;

@Mapper
public interface LoginMapper {

    LoginResp login(LoginReq loginReq);

    Long getUserIdByEmail(String email);

    String getUserRole(Long userId);

    List<String> getUserUrlsAllowed(Long userId);

}
