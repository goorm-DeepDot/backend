package project.deepdot.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.auth.api.dto.request.LoginRequestDto;
import project.deepdot.auth.api.dto.response.LoginResponseDto;
import project.deepdot.auth.api.dto.request.SignUpRequestDto;
import project.deepdot.global.jwt.TokenProvider;
import project.deepdot.user.domain.Role;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.repository.UserRefreshTokenRepository;
import project.deepdot.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    // 회원가입 + 토큰 발급
    @Transactional
    public LoginResponseDto signUp(SignUpRequestDto requestDto) {
        if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        User user = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String accessToken = tokenProvider.createAccessToken(user);
        String refreshToken = tokenProvider.createRefreshToken(user);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .build();
    }

    // 로그인 + 토큰 발급
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = tokenProvider.createAccessToken(user);
        String refreshToken = tokenProvider.createRefreshToken(user);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .build();
    }

    // 로그아웃 전달받은 refreshToken만 삭제
    @Transactional
    public void logoutByRefreshToken(String refreshToken) {
        userRefreshTokenRepository.findByRefreshToken(refreshToken)
                .ifPresent(userRefreshTokenRepository::delete);
        // 존재하지 않아도 에러 없이 멱등성(동일한 연산을 여러 번 수행해도 결과가 변하지 않는 것) 유지
    }
}