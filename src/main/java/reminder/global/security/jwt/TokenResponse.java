package reminder.global.security.jwt;

public record TokenResponse(
        String accessToken,
        Long expiredAt
) {
}
