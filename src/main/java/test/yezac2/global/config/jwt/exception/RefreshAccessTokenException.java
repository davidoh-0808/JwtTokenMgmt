package test.yezac2.global.config.jwt.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN,
        reason = "다음 원인들 중 하나로 인해 Access Token 을 재발급 받을 수 없습니다 (서버 로그 확인 필요): " +
                "1. 입력받은 Refresh Token 에 이상이 있음 " +
                "2. 입력받은 로그인 정보가 DB 정보와 불일치" +
                "3. DB select , insert 에러 발생")
public class RefreshAccessTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RefreshAccessTokenException(String msg) {
        super( msg );
    }

}