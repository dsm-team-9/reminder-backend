package reminder.reminderbe.domain.user.exception;

import reminder.reminderbe.global.error.CustomException;
import reminder.reminderbe.global.error.ErrorCode;

public class UserNotFoundException extends CustomException {

    public static final CustomException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}