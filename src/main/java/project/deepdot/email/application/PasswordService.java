package project.deepdot.email.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final EmailSendService emailService;
    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final long EXPIRE_MINUTES = 5;

    public void sendResetCode(String username, String email) {
        userRepository.findByUsernameAndEmail(username, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));
        String code = String.format("%06d", new java.util.Random().nextInt(999999));
        redisTemplate.opsForValue().set(buildKey(username, email), code, EXPIRE_MINUTES, java.util.concurrent.TimeUnit.MINUTES);
        emailService.sendVerificationCode(email, code);
    }

    public void verifyResetCode(String username, String email, String code) {
        String key = buildKey(username, email);
        String stored = redisTemplate.opsForValue().get(key);
        if (stored == null || !stored.equals(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증코드가 유효하지 않거나 만료되었습니다.");
        }
        redisTemplate.delete(key);
        // OK 마커 10분
        redisTemplate.opsForValue().set(buildOkKey(username, email), "OK", 10, java.util.concurrent.TimeUnit.MINUTES);
    }

    @Transactional
    public void resetPassword(String username, String email, String newPassword) {
        // OK 마커 확인
        String ok = redisTemplate.opsForValue().get(buildOkKey(username, email));
        if (!"OK".equals(ok)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호 재설정 권한이 없거나 만료되었습니다.");
        }

        User user = userRepository.findByUsernameAndEmail(username, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));

        user.changePassword(passwordEncoder.encode(newPassword));
        redisTemplate.delete(buildOkKey(username, email)); // 일회성
    }

    private String buildKey(String u, String e)   { return "reset:pw:"    + u + ":" + e; }
    private String buildOkKey(String u, String e) { return "reset:pw:ok:" + u + ":" + e; }
}