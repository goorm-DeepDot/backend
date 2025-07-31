package project.deepdot.user.api.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequestDto  {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
