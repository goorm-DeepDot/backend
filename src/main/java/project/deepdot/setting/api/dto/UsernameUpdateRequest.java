package project.deepdot.setting.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameUpdateRequest {
    @NotBlank
    private String username;
}
