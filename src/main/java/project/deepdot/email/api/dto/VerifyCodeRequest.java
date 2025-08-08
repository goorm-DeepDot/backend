package project.deepdot.email.api.dto;

import jakarta.validation.constraints.NotBlank;

// 이메일이랑 코드 인증하기.
public record VerifyCodeRequest(
        @NotBlank String username,
        @NotBlank String email,
        @NotBlank String code
) {}