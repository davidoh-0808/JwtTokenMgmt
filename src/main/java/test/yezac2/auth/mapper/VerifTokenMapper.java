package test.yezac2.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import test.yezac2.auth.model.VerifToken;
import test.yezac2.auth.payload.VerifTokenReq;

import java.util.Optional;

@Mapper
public interface VerifTokenMapper {

     int saveVerifToken(VerifTokenReq req);
     Optional<VerifToken> getVerifToken(VerifTokenReq req);
     int setVerifiedAt(VerifTokenReq req);

}
