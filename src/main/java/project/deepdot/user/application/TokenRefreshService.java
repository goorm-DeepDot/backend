package project.deepdot.user.application;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.global.jwt.RefreshAccessTokenDto;
import project.deepdot.global.jwt.RefreshTokenParseDto;
import project.deepdot.global.jwt.TokenProvider;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenRefreshService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public RefreshAccessTokenDto refreshAccessToken(String refreshToken) {

        RefreshTokenParseDto refreshTokenParseDto = refreshTokenParsing(refreshToken);

        User user = userRepository.findById(refreshTokenParseDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));

        String refreshAccessToken;

        try {
            refreshAccessToken = tokenProvider.createAccessToken(user);
        } catch (Exception e) {
            throw new IllegalStateException("토큰 생성에 실패했습니다.", e);
        }

        return RefreshAccessTokenDto.builder()
                .refreshAccessToken(refreshAccessToken)
                .build();
    }

    private RefreshTokenParseDto refreshTokenParsing(String refreshToken) {
        Claims claims = tokenProvider.getClaimsFromToken(refreshToken);

        Long userId = Long.parseLong(claims.getSubject());

        return RefreshTokenParseDto.builder()
                .userId(userId)
                .build();
    }
}
