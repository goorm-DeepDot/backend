package project.deepdot.user.signup.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.deepdot.user.signup.application.UserSignUpService;
import project.deepdot.user.user.domain.repository.UserRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserSignUpController {
    private final UserSignUpService userService;
    private final UserRepository userRepository;

    //회원가입 요청
    @PostMapping("signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid UserSignUpRequestDto requestDto) {
        userService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //아이디 중복 확인
    //예시: /api/user/check-username?username=xxx
    @GetMapping("/check-username")
    public ResponseEntity<Void> checkUsername(@RequestParam String username) {
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 중복
        }
        return ResponseEntity.ok().build(); // 200 사용 가능
    }
}