package project.deepdot.email.application;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// email 보내는 로직
@Service
@RequiredArgsConstructor
public class EmailSendService {

    private final JavaMailSender mailSender;

    public void sendVerificationCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("[DeepDot] 이메일 인증코드입니다.");
        message.setText("인증코드: " + code);
        mailSender.send(message);
    }
}