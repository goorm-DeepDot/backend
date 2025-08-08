package project.deepdot.email.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.deepdot.email.api.dto.SendEmailRequest;
import project.deepdot.email.api.dto.UsernameResponse;
import project.deepdot.email.api.dto.VerifyCodeRequest;
import project.deepdot.email.application.EmailVerificationService;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {


    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@RequestBody SendEmailRequest request) {
        emailVerificationService.sendCode(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<UsernameResponse> verify(@RequestBody VerifyCodeRequest request) {
        String username = emailVerificationService.verifyAndFindUsername(request.email(), request.code());
        return ResponseEntity.ok(new UsernameResponse(username));
    }
}