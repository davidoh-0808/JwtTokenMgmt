package test.yezac2.user.service;

import test.yezac2.user.dto.LoginReq;
import test.yezac2.user.dto.LoginResp;

public interface LoginService {

    LoginResp login(LoginReq loginReq);

    Long getUserIdByEmail(String email);

}
