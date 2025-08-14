package project.deepdot.email.application;

import jakarta.mail.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// email 보내는 로직
@Service
@RequiredArgsConstructor
public class EmailSendService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendVerificationCode(String to, String code) {
        var message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(Message.RecipientType.TO, to);
            message.setSubject("[DeepDot] 이메일 인증 코드");
            String body = """
                    <h3>요청하신 인증 번호입니다.</h3>
                    <h1>%s</h1>
                    <p>해당 코드는 %d분 동안 유효합니다.</p>
                    """.formatted(code, 5);
            message.setText(body, "UTF-8", "html");
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new IllegalStateException("메일 발송 중 오류가 발생했습니다.", e);
        }
    }
}