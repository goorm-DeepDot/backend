package project.deepdot.user.application.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import project.deepdot.user.application.RedisService;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    private String buildKey(String email) {
        return "CERTIFIED:" + email;
    }

    @Override
    public void saveEmailCertification(String email) {
        redisTemplate.opsForValue().set(
                buildKey(email),
                "true",
                Duration.ofMinutes(5) // 인증 상태 유효시간 5분
        );
    }

    @Override
    public boolean isCertified(String email) {
        String value = redisTemplate.opsForValue().get(buildKey(email));
        return "true".equals(value);
    }

    @Override
    public void deleteCertification(String email) {
        redisTemplate.delete(buildKey(email));
    }
}

