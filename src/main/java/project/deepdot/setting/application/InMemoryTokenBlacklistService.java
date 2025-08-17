package project.deepdot.setting.application;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTokenBlacklistService implements TokenBlacklistService {

    private final Map<String, Long> store = new ConcurrentHashMap<>();

    @Override
    public void blacklist(String token, long ttlSeconds) {
        if (token == null || token.isBlank()) return;
        long expiresAt = Instant.now().getEpochSecond() + Math.max(1, ttlSeconds);
        store.put(token, expiresAt);
    }

    @Override
    public boolean isBlacklisted(String token) {
        if (token == null) return false;
        Long exp = store.get(token);
        if (exp == null) return false;
        if (exp < Instant.now().getEpochSecond()) {
            store.remove(token);
            return false;
        }
        return true;
    }
}
