package test.yezac2.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import test.yezac2.user.domain.VerifToken;
import test.yezac2.user.dto.VerifTokenReq;

import java.util.Optional;

@Mapper
public interface VerifTokenMapper {

     int saveVerifToken(VerifTokenReq req);
     Optional<VerifToken> getVerifToken(VerifTokenReq req);
     int setVerifiedAt(VerifTokenReq req);

}
