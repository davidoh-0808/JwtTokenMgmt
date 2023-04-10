package test.yezac2.test;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import test.yezac2.global.common.ApiResp;

@Slf4j
@RequiredArgsConstructor
@RestController
//@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    // JwtTokenVerifier 에서 체크해서 통과되는 api (user id : 2 사용)
    @GetMapping("/schedule/hospital/daily/test/jwt")
    public ResponseEntity<ApiResp> testJwtVerifier1(HttpServletRequest servletReq) {
        String rsMsg = String.format("다음 request URI / URL 로 호출된 테스트 API [%s / %s]"
                                    , servletReq.getRequestURI()
                                    , servletReq.getRequestURL());
        log.info( rsMsg );

        return ResponseEntity.ok(
                    ApiResp.builder()
                    .resultMessage( rsMsg )
                    .build()
        );
    }

    // JwtTokenVerifier 에서 걸려서 통과 못하는 api URI (user id : 2 사용)
    /*
    '/schedule/hospital/daily'
    '/schedule/hospital/monthly'
    '/schedule/hospital/personal'
    '/examination/consignment/send'
    '/examination/consignment/history'

     안되는 URI
     /marketing/manage-admin
     */
    @GetMapping("/marketing/manage-admin/test/jwt")
    public ResponseEntity<ApiResp> testJwtVerifier2(HttpServletRequest servletReq) {
        String rsMsg = String.format("다음 request URI / URL 로 호출된 테스트 API [%s / %s]"
                , servletReq.getRequestURI()
                , servletReq.getRequestURL());
        log.info( rsMsg );

        return ResponseEntity.ok(
                ApiResp.builder()
                        .resultMessage( rsMsg )
                        .build()
        );
    }

    @GetMapping("/management-support/contacts/test/jwt")
    public ResponseEntity<ApiResp> testJwtVerifier3(HttpServletRequest servletReq) {
        String rsMsg = String.format("다음 request URI / URL 로 호출된 테스트 API [%s / %s]"
                , servletReq.getRequestURI()
                , servletReq.getRequestURL());
        log.info( rsMsg );

        return ResponseEntity.ok(
                ApiResp.builder()
                        .resultMessage( rsMsg )
                        .build()
        );
    }


}
