package project.deepdot.user.application;

public interface RedisService {
    void saveEmailCertification(String email);
    boolean isCertified(String email);
    void deleteCertification(String email); // optional
}

