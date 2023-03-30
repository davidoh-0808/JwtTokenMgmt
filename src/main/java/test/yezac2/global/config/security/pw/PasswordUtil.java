package test.yezac2.global.config.security.pw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class PasswordUtil {

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(7);

    private PasswordUtil() {}

    public static PasswordEncoder getPasswordEncoder() {
        // strength 에 높은숫자를 입력 할 수록 해싱 횟수 증가 -> 패스워드 디코딩 시간 증가
        return passwordEncoder;
    }

}
