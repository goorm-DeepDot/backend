package project.deepdot.auth.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.deepdot.user.domain.Role;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    private Long userId;

    @NotBlank(message = "아이디를 입력해주세요")
    private String username;

    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요")
    private String confirmPassword;

    private Role role;
}