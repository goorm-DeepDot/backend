package project.deepdot.email.application;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

// email 보내는 로직
@Service
@RequiredArgsConstructor
public class EmailSendService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendVerificationCode(String to, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            // MimeMessageHelper 사용 → 인코딩 & HTML 안전 처리
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(new InternetAddress(senderEmail));
            helper.setTo(to);
            helper.setSubject("[DeepDot] 이메일 인증 코드");

            String body = """
                    <h3>요청하신 인증 번호입니다.</h3>
                    <h1>%s</h1>
                    <p>해당 코드는 %d분 동안 유효합니다.</p>
                    """.formatted(code, 5);

            helper.setText(body, true); // true = HTML 본문

            javaMailSender.send(message);
        } catch (Exception e) {
            // 원인 파악을 위해 e 로그는 반드시 남겨야 함
            throw new IllegalStateException("메일 발송 중 오류가 발생했습니다.", e);
        }
    }
}