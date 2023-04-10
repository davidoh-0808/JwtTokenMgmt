package test.yezac2.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.yezac2.global.common.ApiResp;
import test.yezac2.auth.payload.LoginReq;
import test.yezac2.auth.payload.LoginResp;
import test.yezac2.global.config.jwt.payload.AccessTokenRefreshReq;
import test.yezac2.global.config.jwt.payload.AccessTokenRefreshResp;
import test.yezac2.auth.service.LoginService;
import test.yezac2.global.config.jwt.service.TokenService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;
    private final TokenService tokenService;


    @Operation(summary = "유저 로그인"
            , description = "아이디/패스워드로 로그인 요청 -> refresh token () / access token () 발급 받음"
            , tags = { "로그인", "post", "email", "password", "jwt", "access token", "refresh token" })
    @ApiResponses({
        @ApiResponse( responseCode = "200"
                    , description = "로그인 성공"
                    , content = { @Content(schema = @Schema(implementation = LoginResp.class) , mediaType = "application/json") })
        , @ApiResponse(responseCode = "500"
                    , description = "서버 에러"
                    , content = { @Content(schema = @Schema()) }) })
    @PostMapping("/auth")
    public ResponseEntity<ApiResp<LoginResp>> login(@RequestBody LoginReq loginReq) {
        // access token (jwt) 리턴
        LoginResp loginResp = loginService.login(loginReq);

        return ResponseEntity.ok(
                ApiResp.<LoginResp> builder()
                        .data( loginResp )
                        .resultMessage(
                            String.format("유저[id:%d | email:%s] 로그인 성공", loginResp.getId(), loginResp.getEmail())
                        )
                        .build()
        );
    }


    @Operation(summary = "만료된 유저 access token 재발급"
            , description = "아이디/패스워드로 로그인 요청 -> refresh token () / access token () 발급 받음"
            , tags = { "로그인", "post", "email", "password", "jwt", "access token", "refresh token" })
    @ApiResponses({
        @ApiResponse( responseCode = "200"
                    , description = "로그인 성공"
                    , content = { @Content(schema = @Schema(implementation = LoginResp.class) , mediaType = "application/json") })
        , @ApiResponse( responseCode = "401"
                    , description = "401 UNAUTHORIZED status error : 액세스 토큰 혹은 액세스 토큰 재발급용 리프레시 토큰에 이상 / 토큰과 함께 요청하지 않음"
                    , content = { @Content(schema = @Schema()) })
        , @ApiResponse( responseCode = "403"
                    , description = "403 FORBIDDEN status error : 유저가 해당 url 에 대한 권한이 없음"
                    , content = { @Content(schema = @Schema()) })
        , @ApiResponse( responseCode = "500"
                    , description = "서버 에러"
                    , content = { @Content(schema = @Schema()) }) })
    @PostMapping("/auth/refreshAccessToken")
    public ResponseEntity<ApiResp<AccessTokenRefreshResp>> refreshAccessToken(@RequestBody AccessTokenRefreshReq req) {
        // refresh token 을 받아 만료된 access token (jwt) 를 재발급 받는다
        AccessTokenRefreshResp resp = tokenService.refreshAccessToken( req );

        return ResponseEntity.ok(
            ApiResp.<AccessTokenRefreshResp> builder()
                    .data( resp )
                    .resultMessage(String.format("토큰 재발급에 성공했습니다"))
                    .build()
        );
    }


}