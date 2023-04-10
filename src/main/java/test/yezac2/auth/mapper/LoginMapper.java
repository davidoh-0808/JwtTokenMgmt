package test.yezac2.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import test.yezac2.auth.payload.LoginReq;
import test.yezac2.auth.payload.LoginResp;

import java.util.List;

@Mapper
public interface LoginMapper {

    LoginResp login(LoginReq loginReq);

    long getUserIdByEmail(String email);
    String getEmailByUserId(long userId);

    String getUserRole(long userId);

    List<String> getUserUrlsAllowed(long userId);

}
