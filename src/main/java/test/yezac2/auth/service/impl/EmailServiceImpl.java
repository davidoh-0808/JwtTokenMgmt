package test.yezac2.auth.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import test.yezac2.auth.service.EmailService;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${user_verification.yezac_veri_email}")
    private String YEZAC_VERI_EMAIL;

    private final JavaMailSender javaMailSender;


    @Override
    @Async      // background thread 사용
    public void send(String toEmail, String emailContent) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailContent, true);
            helper.setTo(toEmail);
            helper.setSubject("입력하신 이메일을 인증해주세요.");
            helper.setFrom(YEZAC_VERI_EMAIL);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error(String.format("이메일 전송 불가 [toEmail : %s]", toEmail));
            throw new IllegalStateException("failed to send email");
        }
    }


}
