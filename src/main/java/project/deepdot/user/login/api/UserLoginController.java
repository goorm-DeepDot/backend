package project.deepdot.user.login.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.deepdot.user.login.application.UserLoginService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserLoginController {

    private UserLoginService userLoginService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        UserLoginResponseDto responseDto = userLoginService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
