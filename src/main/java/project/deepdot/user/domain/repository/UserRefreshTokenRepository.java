package project.deepdot.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import project.deepdot.user.domain.UserRefreshToken;

import java.util.Optional;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {

    // 사용자 기준으로 리프레시 토큰 조회
    Optional<UserRefreshToken> findByUser(User user);

    // 리프레시 토큰 문자열로 조회 (역방향 확인용)
    Optional<UserRefreshToken> findByRefreshToken(String refreshToken);

    // 사용자 기준으로 삭제
    void deleteByUser(User user);
}