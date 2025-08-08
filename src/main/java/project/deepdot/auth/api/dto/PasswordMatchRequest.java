package project.deepdot.auth.api.dto;

public record PasswordMatchRequest(
        String password,
        String confirmPassword
) { }