package project.deepdot.email.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// 이메일 보내기
public record SendEmailRequest(
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email
) {}