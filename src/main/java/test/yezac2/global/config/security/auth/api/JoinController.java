package test.yezac2.global.config.security.auth.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.yezac2.global.common.ApiResponse;
import test.yezac2.global.config.security.auth.dto.JoinReq;
import test.yezac2.global.config.security.auth.dto.JoinResp;
import test.yezac2.global.config.security.auth.service.JoinService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("user/join")
public class JoinController {

    private final JoinService joinService;


    @PostMapping
    public ResponseEntity< ApiResponse<JoinResp> > join(@RequestBody JoinReq req) {

        String verifToken = joinService.joinUser(req);

        JoinResp resp = JoinResp.builder()
                .verifToken( verifToken )
                .build();

        return ResponseEntity.ok(
                ApiResponse.<JoinResp>builder()
                        .data( resp )
                        .resultMessage("user join success")
                        .build()
        );
    }


    @GetMapping(value = "/verify")
    public ResponseEntity<ApiResponse> verify(@RequestParam("token") String tokenOnLink) {

        String resultMsg = joinService.verifyToken(tokenOnLink);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .resultMessage(resultMsg)
                        .build()
        );
    }


}
