package project.deepdot.auth.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.deepdot.auth.api.dto.LoginRequestDto;
import project.deepdot.auth.api.dto.LoginResponseDto;
import project.deepdot.auth.api.dto.PasswordMatchRequest;
import project.deepdot.auth.api.dto.SignUpRequestDto;
import project.deepdot.auth.application.AuthService;
import project.deepdot.user.domain.repository.UserRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    //회원가입 요청
    @PostMapping("/signup")
    public ResponseEntity<LoginResponseDto> signUp(@RequestBody @Valid SignUpRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(dto));
    }

    //아이디 중복 확인
    //예시: /api/user/check-username?username=xxx
    @GetMapping("/check-username")
    public ResponseEntity<Void> checkUsername(@RequestParam String username) {
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 중복
        }
        return ResponseEntity.ok().build();
    }

    // 비밀번호 확인
    @PostMapping("/check-match")
    public ResponseEntity<Void> checkPasswordMatch(@RequestBody PasswordMatchRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            return ResponseEntity.badRequest().build(); // 400 비밀번호 불일치
        }
        return ResponseEntity.ok().build(); // 200 비밀번호 일치
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        LoginResponseDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}