package test.yezac2.user.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.yezac2.global.common.ApiResponse;
import test.yezac2.user.dto.LoginReq;
import test.yezac2.user.dto.LoginResp;
import test.yezac2.user.service.LoginService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/auth")
    public ResponseEntity<ApiResponse<LoginResp>> login(@RequestBody LoginReq loginReq) {
        // jwt token 리턴
        LoginResp loginResp = loginService.login(loginReq);

        // TODO: CHECK ->
        //  fixed : securityfilterchain continues to inject JwttokenVerifier after login resp is retrieved
        return ResponseEntity.ok(
                ApiResponse.<LoginResp> builder()
                        .data( loginResp )
                        .resultMessage("user login success")
                        .build()
        );
    }

}