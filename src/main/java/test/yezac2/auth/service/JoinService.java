package test.yezac2.auth.service;


import test.yezac2.auth.payload.JoinReq;

public interface JoinService {

    /**
     * return verif_token 인증 토큰
     */
    String joinUser(JoinReq req);

    String verifyToken(String tokenOnLink);

}
