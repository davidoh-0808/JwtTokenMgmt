package test.yezac2.global.config.jwt.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.UNAUTHORIZED,
    reason = "요청 시 헤더에 입력한 access token 이 만료되었습니다." +
            "보관하고 있는 refresh token 을 사용하여 " +
            "/auth/refreshAccessToken 을 호출하면 access token 을 재발급 받을 수 있습니다."
)
public class AccessTokenExpiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccessTokenExpiredException(String msg) {
        super( msg );
    }

}
