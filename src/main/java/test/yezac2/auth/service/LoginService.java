package test.yezac2.auth.service;

import test.yezac2.auth.payload.LoginReq;
import test.yezac2.auth.payload.LoginResp;

public interface LoginService {

    LoginResp login(LoginReq loginReq);

    Long getUserIdByEmail(String email);

}
