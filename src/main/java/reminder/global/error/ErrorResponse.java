package reminder.global.error;

public record ErrorResponse(
        Integer status,
        String message
) {

}