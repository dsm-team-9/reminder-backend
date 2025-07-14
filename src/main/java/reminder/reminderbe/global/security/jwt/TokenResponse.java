package reminder.reminderbe.global.security.jwt;

public record TokenResponse(
        String accessToken,
        Long expiredAt
) {
}
