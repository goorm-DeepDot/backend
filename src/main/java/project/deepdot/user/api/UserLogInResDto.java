package project.deepdot.user.api;

import lombok.Builder;

@Builder
public record UserLogInResDto(
        String accessToken,
        String refreshToken
) {
}