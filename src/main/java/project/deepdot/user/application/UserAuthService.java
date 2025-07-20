package project.deepdot.user.application;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.global.jwt.TokenProvider;
import project.deepdot.user.api.UserInfo;
import project.deepdot.user.api.UserInfoRes;
import project.deepdot.user.api.UserLogInResDto;
import project.deepdot.user.domain.UserRefreshToken;
import project.deepdot.user.domain.repository.UserRefreshTokenRepository;
import project.deepdot.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuthService {
    private final UserRepository userRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private final TokenProvider tokenProvider;
    private final TokenRefreshService tokenRefreshService;

    /*@Transactional
    public UserLogInResDto logIn(UserLogInReqDto request) {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // renewRefreshToken
        String createRefreshToken = tokenProvider.createRefreshToken(user);

        // refreshTokenEntity
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUser_UserId(user.getUserId())
                .orElseGet(() -> {
                    UserRefreshToken logoutUserRefreshToken = new UserRefreshToken();
                    logoutUserRefreshToken.setUser(user);
                    return userRefreshTokenRepository.save(logoutUserRefreshToken);
                });

        userRefreshToken.setRefreshToken(createRefreshToken);
        userRefreshToken.setUser(user);

        userRefreshTokenRepository.save(userRefreshToken);

        RefreshAccessTokenDto refreshAccessTokenDto = tokenRefreshService.refreshAccessToken(createRefreshToken);

        // renewAccessToken
        String refreshAccessToken = refreshAccessTokenDto.refreshAccessToken();

        UserLogInResDto userLogInResDto = UserLogInResDto.builder()
                .accessToken(refreshAccessToken)
                .refreshToken(createRefreshToken)
                .build();

        return userLogInResDto;
    }*/


//    public UserInfoRes getUserInfo(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));
//
//        return UserInfoRes.builder()
//                .userId(userId)
//                .name(user.getName())
//                .email(user.getEmail())
//                .build();
//    }
//
//    @Transactional
//    public void logout(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));
//
//        userRefreshTokenRepository.deleteByUserImmediate(user);
//    }
}