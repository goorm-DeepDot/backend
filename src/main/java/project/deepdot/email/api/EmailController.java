package project.deepdot.email.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.deepdot.email.api.dto.SendEmailRequest;
import project.deepdot.email.api.dto.id.UsernameResponse;
import project.deepdot.email.api.dto.VerifyCodeRequest;
import project.deepdot.email.application.EmailVerificationService;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailVerificationService emailVerificationService;

    // 인증코드 전송 (회원가입/아이디찾기 공통 사용 가능)
    @PostMapping("/send-code")
    public ResponseEntity<Void> send(@RequestBody SendEmailRequest request) {
        emailVerificationService.sendCode(request.email());
        return ResponseEntity.ok().build();
    }

    // 아이디 찾기: 이메일 + 인증코드 → username 반환
    @PostMapping("/verify-id")
    public ResponseEntity<UsernameResponse> verifyAndFindUsername(@RequestBody VerifyCodeRequest request) {
        String username = emailVerificationService.verifyAndFindUsername(request.email(), request.code());
        return ResponseEntity.ok(new UsernameResponse(username));
    }

    // 회원가입: username + email + 인증코드 → 인증만 수행 (반환 없음)
    @PostMapping("/verify-signup")
    public ResponseEntity<Void> verifyForSignup(@RequestBody VerifyCodeRequest request) {
        emailVerificationService.verifyForSignup(request.username(), request.email(), request.code());
        return ResponseEntity.ok().build();
    }
}