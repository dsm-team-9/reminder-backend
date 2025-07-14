package reminder.reminderbe.domain.user.exception;

import reminder.reminderbe.global.error.CustomException;
import reminder.reminderbe.global.error.ErrorCode;

public class PasswordMissMatchException extends CustomException {

    public static final CustomException EXCEPTION = new PasswordMissMatchException();

    private PasswordMissMatchException(){
        super(ErrorCode.PASSWORD_MISS_MATCH);
    }

}