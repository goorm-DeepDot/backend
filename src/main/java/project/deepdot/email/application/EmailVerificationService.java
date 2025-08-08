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
        redisTemplate.opsForValue().set(buildKey(email), code, EXPIRE_MINUTES, TimeUnit.MINUTES);
        emailService.sendVerificationCode(email, code);
    }

    // 아이디 찾기용: email + code → username 반환
    public String verifyAndFindUsername(String email, String code) {
        String stored = redisTemplate.opsForValue().get(buildKey(email));
        if (stored == null || !stored.equals(code)) {
            throw new IllegalArgumentException("인증코드가 유효하지 않거나 만료되었습니다.");
        }
        redisTemplate.delete(buildKey(email));

        return userRepository.findByEmail(email)
                .map(User::getUsername)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 가입된 사용자가 없습니다."));
    }

    // 회원가입용: username + email + code → 검증만 수행
    public void verifyForSignup(String username, String email, String code) {
        String stored = redisTemplate.opsForValue().get(buildKey(email));
        if (stored == null || !stored.equals(code)) {
            throw new IllegalArgumentException("인증코드가 유효하지 않거나 만료되었습니다.");
        }
        redisTemplate.delete(buildKey(email));

        // username + email이 일치하는 사용자 없는 경우 = 회원가입 가능 상태 (예외 안 던짐)
        boolean exists = userRepository.existsByUsernameOrEmail(username, email);
        if (exists) {
            throw new IllegalArgumentException("이미 사용 중인 아이디 또는 이메일입니다.");
        }
    }

    private String buildKey(String email) {
        return "verify:email:" + email;
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}