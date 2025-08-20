package project.deepdot.auth.api.dto.request;

public record PasswordMatchRequest(
        String password,
        String confirmPassword
) { }