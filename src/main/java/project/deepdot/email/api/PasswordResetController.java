package project.deepdot.email.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.deepdot.email.api.dto.VerifyCodeRequest;
import project.deepdot.email.api.dto.passwrod.ChangePasswordRequest;
import project.deepdot.email.api.dto.passwrod.PasswordResetRequest;
import project.deepdot.email.application.PasswordResetService;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    // 인증 코드 이메일 발송
    @PostMapping("/send-code")
    public ResponseEntity<Void> sendCode(@RequestBody PasswordResetRequest request) {
        passwordResetService.sendResetCode(request.username(), request.email());
        return ResponseEntity.ok().build();
    }

    // 인증 코드 유효성 검증
    @PostMapping("/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody VerifyCodeRequest request) {
        passwordResetService.verifyResetCode(request.username(), request.email(), request.code());
        return ResponseEntity.ok().build();
    }

    // 비밀번호 변경(새로운 비밀번호 저장)
    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody ChangePasswordRequest request) {
        passwordResetService.resetPassword(request.username(), request.newPassword());
        return ResponseEntity.ok().build();
    }
}