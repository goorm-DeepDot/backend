package project.deepdot.global.jwt;

import lombok.Builder;

@Builder
public record RefreshAccessTokenDto(
        String refreshAccessToken
) {
}