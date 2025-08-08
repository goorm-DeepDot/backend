package project.deepdot.user.api.token;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefreshAccessTokenDto {
    private String refreshAccessToken;
}