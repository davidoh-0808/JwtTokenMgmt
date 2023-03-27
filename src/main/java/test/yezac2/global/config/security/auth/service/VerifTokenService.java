package test.yezac2.global.config.security.auth.service;

import test.yezac2.global.config.security.auth.domain.VerifToken;
import test.yezac2.global.config.security.auth.dto.VerifTokenReq;

import java.util.Optional;

public interface VerifTokenService {

    int saveVerifToken(VerifTokenReq dto);

    Optional<VerifToken> getVerifToken(VerifTokenReq req);

    int setVerifiedAt(VerifTokenReq req);


}
