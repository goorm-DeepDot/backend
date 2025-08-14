package project.deepdot.email.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.UserRepository;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailSendService emailService;
    private final StringRedisTemplate redis;
    private final UserRepository userRepository;

    // ===== 설정 =====
    private static final int CODE_LENGTH = 6;
    private static final Duration CODE_TTL = Duration.ofMinutes(5);   // 코드 만료
    private static final Duration RESEND_COOLDOWN = Duration.ofSeconds(60); // 재발송 쿨다운
    private static final int MAX_ATTEMPTS = 5;

    private static final char[] DIGITS = "0123456789".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();

    // ===== 외부 API =====

    /** 인증코드 발송 */
    public void sendCode(String rawEmail) {
        String email = normalizeEmail(rawEmail);

        // 쿨다운 확인
        String cooldownKey = cooldownKey(email);
        Boolean cooling = redis.hasKey(cooldownKey);
        if (Boolean.TRUE.equals(cooling)) {
            throw new IllegalStateException("잠시 후 다시 시도하세요. (재발송 쿨다운 중)");
        }

        // 코드 생성 & 저장
        String code = generateCode();
        String codeKey = codeKey(email);
        redis.opsForValue().set(codeKey, code, CODE_TTL);

        // 시도횟수 초기화
        redis.delete(attemptKey(email));

        // 쿨다운 걸기
        redis.opsForValue().set(cooldownKey, "1", RESEND_COOLDOWN);

        // 메일 발송 (템플릿은 EmailSendService 내에서 처리)
        emailService.sendVerificationCode(email, code);
    }

    /** 아이디 찾기: email + code → username */
    public String verifyAndFindUsername(String rawEmail, String code) {
        String email = normalizeEmail(rawEmail);
        verifyCode(email, code); // 유효성/시도횟수 처리

        return userRepository.findByEmail(email)
                .map(User::getUsername)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 가입된 사용자가 없습니다."));
    }

    /** 회원가입용: username + email + code → 검증만 수행 */
    public void verifyForSignup(String username, String rawEmail, String code) {
        String email = normalizeEmail(rawEmail);
        verifyCode(email, code);

        boolean exists = userRepository.existsByUsernameOrEmail(username, email);
        if (exists) {
            throw new IllegalArgumentException("이미 사용 중인 아이디 또는 이메일입니다.");
        }
    }

    // ===== 내부 공통 로직 =====

    /** 코드 검증 + 실패 시도 관리 */
    private void verifyCode(String email, String code) {
        String codeKey = codeKey(email);
        String stored = redis.opsForValue().get(codeKey);

        if (stored == null) {
            throw new IllegalArgumentException("인증코드가 유효하지 않거나 만료되었습니다.");
        }

        if (!stored.equals(code)) {
            long attempts = redis.opsForValue().increment(attemptKey(email));
            if (attempts >= MAX_ATTEMPTS) {
                // 너무 많이 틀리면 코드 폐기
                redis.delete(codeKey);
                throw new IllegalStateException("인증 시도 횟수를 초과했습니다. 코드를 다시 받으세요.");
            }
            throw new IllegalArgumentException("인증코드가 일치하지 않습니다.");
        }

        // 성공: 코드/시도횟수 제거
        redis.delete(codeKey);
        redis.delete(attemptKey(email));
    }

    private String generateCode() {
        char[] buf = new char[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            buf[i] = DIGITS[RANDOM.nextInt(DIGITS.length)];
        }
        return new String(buf);
    }

    private String normalizeEmail(String email) {
        if (email == null) throw new IllegalArgumentException("이메일이 비어있습니다.");
        return email.trim().toLowerCase();
    }

    // ===== Redis 키 =====
    private String codeKey(String email)     { return "verify:email:code:" + email; }
    private String attemptKey(String email)  { return "verify:email:attempts:" + email; }
    private String cooldownKey(String email) { return "verify:email:cooldown:" + email; }
}
