package test.yezac2.global.config.security.auth.service;

import test.yezac2.global.config.security.auth.dto.LoginReq;
import test.yezac2.global.config.security.auth.dto.LoginResp;

public interface LoginService {

    LoginResp login(LoginReq loginReq);

    Long getUserIdByEmail(String email);

}
