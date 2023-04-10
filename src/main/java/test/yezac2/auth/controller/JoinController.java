package test.yezac2.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.yezac2.global.common.ApiResp;
import test.yezac2.auth.payload.JoinReq;
import test.yezac2.auth.payload.JoinResp;
import test.yezac2.auth.service.JoinService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("user/join")
public class JoinController {

    private final JoinService joinService;


    @PostMapping
    public ResponseEntity<ApiResp<JoinResp>> join(@RequestBody JoinReq req) {

        String verifToken = joinService.joinUser(req);

        JoinResp resp = JoinResp.builder()
                .verifToken( verifToken )
                .build();

        return ResponseEntity.ok(
                ApiResp.<JoinResp>builder()
                        .data( resp )
                        .resultMessage("user join success")
                        .build()
        );
    }


    @GetMapping(value = "/verify")
    public ResponseEntity<ApiResp> verify(@RequestParam("token") String tokenOnLink) {

        String resultMsg = joinService.verifyToken(tokenOnLink);

        return ResponseEntity.ok(
                ApiResp.<String>builder()
                        .resultMessage(resultMsg)
                        .build()
        );
    }


}
