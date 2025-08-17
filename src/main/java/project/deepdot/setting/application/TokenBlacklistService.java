package project.deepdot.setting.application;

public interface TokenBlacklistService {
    void blacklist(String token, long ttlSeconds);
    boolean isBlacklisted(String token);
}
