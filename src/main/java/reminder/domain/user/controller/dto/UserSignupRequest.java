package reminder.domain.user.controller.dto;

public record UserSignupRequest(
        String name,
        String phoneNumber,
        String password
) {
}
