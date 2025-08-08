package project.deepdot.user.api.token;

import lombok.Builder;

@Builder
public record RefreshTokenParseDto(
        String username
) {}