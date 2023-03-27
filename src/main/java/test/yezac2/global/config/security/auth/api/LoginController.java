package test.yezac2.global.config.security.auth.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.yezac2.global.common.ApiResponse;
import test.yezac2.global.config.security.auth.dto.LoginReq;
import test.yezac2.global.config.security.auth.dto.LoginResp;
import test.yezac2.global.config.security.auth.service.LoginService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class LoginController {

    private final LoginService loginService;

    @PostMapping(path = "/login")
    public ResponseEntity<ApiResponse<LoginResp>> login(@RequestBody LoginReq loginReq) {
        // jwt token 리턴
        LoginResp loginResp = loginService.login(loginReq);

        return ResponseEntity.ok(
                ApiResponse.<LoginResp>builder()
                        .data( loginResp )
                        .resultMessage("user login success")
                        .build()
        );
    }

}