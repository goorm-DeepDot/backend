package project.deepdot.email.api.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyCodeRequest(
        @NotBlank String email,
        @NotBlank String code
) {}