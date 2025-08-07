package project.deepdot.user.signup.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import project.deepdot.user.user.domain.Role;
import project.deepdot.user.user.domain.User;

public class UserSignUpRequestDto {
    private Long userId;

    @NotBlank(message = "아이디를 입력해주세요")
    private String username;

    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    private Role role;

    @Builder
    public User toEntity(){
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(Role.USER)
                .build();
    }
}
