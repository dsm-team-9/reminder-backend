package reminder.domain.user.controller.dto;

public record UserLoginRequest(
        String phoneNumber,
        String password
) {
}
