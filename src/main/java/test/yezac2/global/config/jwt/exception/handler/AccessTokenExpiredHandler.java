package test.yezac2.global.config.jwt.exception.handler;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.yezac2.global.config.jwt.exception.AccessTokenExpiredException;


// @ControllerAdvice
// public class AccessTokenExpiredHandler extends ResponseEntityExceptionHandler {
@Slf4j
@RestControllerAdvice
public class AccessTokenExpiredHandler {

    @ExceptionHandler({
            AccessTokenExpiredException.class
    })
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "요청 시 헤더에 입력한 access token 이 만료되었습니다.")
    public static ResponseEntity handleAccessTokenExpiration(
            final AccessTokenExpiredException ex, final HttpServletRequest servletReq) {

        // 프론트 단에서 401 UNAUTHORIZED 상태값과 어떻게 처리할지 안내 메시지를 보내준다
        // String guideMsg = "요청 시 헤더에 입력한 access token 이 만료되었습니다. 보관하고 있는 refresh token 을 사용하여 /auth/refreshAccessToken 을 호출하면 access token 을 재발급 받을 수 있습니다.";
        String guideMsg = ex.getLocalizedMessage();
        log.error("만료된 Access Token 서버 로그: %s", ex.getMessage());
        log.error( guideMsg );

        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( guideMsg );
    }

}
