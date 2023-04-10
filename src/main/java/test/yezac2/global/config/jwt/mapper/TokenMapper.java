package test.yezac2.global.config.jwt.mapper;

import org.apache.ibatis.annotations.Mapper;
import test.yezac2.global.config.jwt.payload.AccessTokenRefreshReq;
import test.yezac2.global.config.jwt.payload.AccessTokenIssueReq;
import test.yezac2.global.config.jwt.payload.RefreshTokenIssueReq;

@Mapper
public interface TokenMapper {

    boolean issueRefreshToken(RefreshTokenIssueReq req);
    String getRefreshTokenPresent(long userId);
    boolean deleteRefreshTokenExpired(long userId);
    boolean issueAccessToken(AccessTokenIssueReq req);
    boolean isRefreshTokenPresent(long userId);
    boolean isRefreshTokenExpired(String refreshToken);
    boolean isRefreshtokenExpiredByUserId(long userId);
    boolean deleteExistingAccessToken(String refreshToken);
    boolean isAccessTokenPresent(long userId);
    long getUserIdFromRefreshToken(String refreshToken);
    boolean deleteExistingAccessTokenByUserId(long userId);
    boolean reissueAccessToken(AccessTokenRefreshReq req);



}
