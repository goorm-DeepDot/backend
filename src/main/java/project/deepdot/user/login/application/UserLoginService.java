package project.deepdot.user.login.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.deepdot.global.jwt.TokenProvider;
import project.deepdot.user.login.api.UserLoginRequestDto;
import project.deepdot.user.login.api.UserLoginResponseDto;
import project.deepdot.user.user.domain.User;
import project.deepdot.user.user.domain.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserLoginResponseDto login(UserLoginRequestDto request) {
        // 1. 유저 조회
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 객체 수동 생성
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null,
                                                        List.of(() -> user.getRole().name())); // 권한 정보

        // 4. 토큰 발급
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        // 5. 응답 DTO 반환
        return UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .build();
    }
}