package project.deepdot.global.jwt;

import lombok.Builder;

@Builder
public record RefreshTokenParseDto(
        Long userId
) {
}
