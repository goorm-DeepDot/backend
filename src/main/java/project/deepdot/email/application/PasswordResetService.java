package project.deepdot.email.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.UserRepository;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final EmailSendService emailService;
    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final long EXPIRE_MINUTES = 5;

    public void sendResetCode(String username, String email) {
        User user = userRepository.findByUsernameAndEmail(username, email)
                .orElseThrow(() -> new IllegalArgumentException("아이디와 이메일이 일치하지 않습니다."));

        String code = String.format("%06d", new Random().nextInt(999999));
        redisTemplate.opsForValue().set(buildKey(username, email), code, EXPIRE_MINUTES, TimeUnit.MINUTES);
        emailService.sendVerificationCode(email, code);
    }

    public void verifyResetCode(String username, String email, String code) {
        String key = buildKey(username, email);
        String stored = redisTemplate.opsForValue().get(key);
        if (stored == null || !stored.equals(code)) {
            throw new IllegalArgumentException("인증코드가 유효하지 않거나 만료되었습니다.");
        }
        redisTemplate.delete(key); // 인증 성공 시 삭제
    }

    public void resetPassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        user.changePassword(passwordEncoder.encode(newPassword));
    }

    private String buildKey(String username, String email) {
        return "reset:pw:" + username + ":" + email;
    }
}