package test.yezac2.global.config.security.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.yezac2.global.config.security.auth.domain.VerifToken;
import test.yezac2.global.config.security.auth.dto.JoinReq;
import test.yezac2.global.config.security.auth.dto.VerifTokenReq;
import test.yezac2.global.config.security.auth.service.AppUserDetailsService;
import test.yezac2.global.config.security.auth.service.EmailService;
import test.yezac2.global.config.security.auth.service.JoinService;
import test.yezac2.global.config.security.auth.service.VerifTokenService;

import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Service
public class JoinServiceImpl implements JoinService {

    private final AppUserDetailsService appUserDetailsService;
    private final VerifTokenService verifTokenService;
    private final EmailService emailService;

    @Value("${user_verification.yezac_proj_baseUrl}")
    private String projBaseUrl;


    @Override
    public String joinUser(JoinReq req) {
        // to finish: regex email

        // join User but user is NOT enabled
        String tokenEmailAndFront = appUserDetailsService.joinAppUser(req);

        // send email w/ the verifLink
        String verifLink = projBaseUrl + "/user/join/verify?token=" + tokenEmailAndFront;
        String emailContent = buildEmail( req.getName(), verifLink );
        emailService.send( req.getEmail(), emailContent);

        return tokenEmailAndFront;
    }


    @Transactional
    @Override
    public String verifyToken(String tokenOnLink) {

        VerifTokenReq req = new VerifTokenReq(tokenOnLink);

        // check if the validity of the token from the email link
        VerifToken verifToken = verifTokenService.getVerifToken( req )
                .orElseThrow(() -> new IllegalStateException(
                    String.format("입력받은 유저 인증토큰을 찾을 수 없습니다 [%s]", req.getToken()))
                );

        if (verifToken.getVerifiedAt() != null) {
            throw new IllegalStateException(
                    String.format("이메일 유저인증 토큰이 이미 완료 되었습니다 [token : %s]", verifToken.getToken())
            );
        }

        if ( verifToken.getExpiresAt().before(new Date()) ) {
            throw new IllegalStateException(
                    String.format("이메일 유저인증 토큰이 만료 되었습니다 [token expiresAt : %s", verifToken.getExpiresAt())
            );
        }

        // finally, confirm the verifToken / enable the newly created user
        verifTokenService.setVerifiedAt( new VerifTokenReq(tokenOnLink) );
        // String usersEmail = verifToken.getUsersEmail();

        // toDO: ******** fix code so that verif_token receives email ********
        appUserDetailsService.enableAppUser( "davidoh72@gmail.com" );

        return "토큰인증 완료 / 유저 활성화 완료";
    }

    private String buildEmail(String name, String verifLink) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + verifLink + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }


}
