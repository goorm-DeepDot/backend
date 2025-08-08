package project.deepdot.email.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import project.deepdot.user.domain.repository.UserRepository;
import project.deepdot.user.domain.User;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailSendService emailService;
    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;

    private static final long EXPIRE_MINUTES = 5;

    public void sendCode(String email) {
        String code = generateCode();

        redisTemplate.opsForValue().set(
                buildKey(email),
                code,
                EXPIRE_MINUTES,
                TimeUnit.MINUTES
        );

        emailService.sendVerificationCode(email, code);
    }

    // 인증 + username 조회 수행
    public String verifyAndFindUsername(String email, String code) {
        String stored = redisTemplate.opsForValue().get(buildKey(email));

        if (stored == null || !stored.equals(code)) {
            throw new IllegalArgumentException("인증코드가 유효하지 않거나 만료되었습니다.");
        }

        redisTemplate.delete(buildKey(email)); // 인증코드는 일회성

        return userRepository.findByEmail(email)
                .map(User::getUsername)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 가입된 사용자가 없습니다."));
    }

    private String buildKey(String email) {
        return "verify:email:" + email;
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}