package test.yezac2.user.service;

import test.yezac2.user.domain.VerifToken;
import test.yezac2.user.dto.VerifTokenReq;

import java.util.Optional;

public interface VerifTokenService {

    int saveVerifToken(VerifTokenReq dto);

    Optional<VerifToken> getVerifToken(VerifTokenReq req);

    int setVerifiedAt(VerifTokenReq req);


}
