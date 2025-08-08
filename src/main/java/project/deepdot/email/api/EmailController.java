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

    //이메일 보내는 주소
    @PostMapping("/send-code")
    public ResponseEntity<Void> send(@RequestBody SendEmailRequest request) {
        emailVerificationService.sendCode(request.email());
        return ResponseEntity.ok().build();
    }

    //이메일 검증하는 주소
    @PostMapping("/verify")
    public ResponseEntity<UsernameResponse> verify(@RequestBody VerifyCodeRequest request) {
        String username = emailVerificationService.verifyAndFindUsername(request.email(), request.code());
        return ResponseEntity.ok(new UsernameResponse(username));
    }
}