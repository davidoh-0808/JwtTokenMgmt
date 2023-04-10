package test.yezac2.auth.service;

import test.yezac2.auth.model.VerifToken;
import test.yezac2.auth.payload.VerifTokenReq;

import java.util.Optional;

public interface VerifTokenService {

    int saveVerifToken(VerifTokenReq dto);

    Optional<VerifToken> getVerifToken(VerifTokenReq req);

    int setVerifiedAt(VerifTokenReq req);


}
