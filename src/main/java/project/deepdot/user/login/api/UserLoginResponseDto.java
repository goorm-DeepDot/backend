package project.deepdot.user.login.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String username;
}