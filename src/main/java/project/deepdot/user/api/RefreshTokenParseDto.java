package project.deepdot.user.api;

import lombok.Builder;

@Builder
public record RefreshTokenParseDto(
        String username
) {}