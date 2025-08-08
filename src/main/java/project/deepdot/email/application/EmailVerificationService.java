package project.deepdot.email.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailService emailService;
    private final StringRedisTemplate redisTemplate;

    private static final long EXPIRE_MINUTES = 5;

    public void sendCode(String email) {
        String code = generateCode();

        // Redis에 저장 (유효시간 5분)
        redisTemplate.opsForValue().set(
                buildKey(email),
                code,
                EXPIRE_MINUTES,
                TimeUnit.MINUTES
        );

        emailService.sendVerificationCode(email, code);
    }

    public void verifyCode(String email, String code) {
        String stored = redisTemplate.opsForValue().get(buildKey(email));

        if (stored == null || !stored.equals(code)) {
            throw new IllegalArgumentException("인증코드가 유효하지 않거나 만료되었습니다.");
        }

        redisTemplate.delete(buildKey(email)); // 일회성 사용
    }

    private String buildKey(String email) {
        return "verify:email:" + email;
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}