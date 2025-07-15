package reminder.domain.user.exception;

import reminder.global.error.CustomException;
import reminder.global.error.ErrorCode;

public class PasswordMissMatchException extends CustomException {

    public static final CustomException EXCEPTION = new PasswordMissMatchException();

    public PasswordMissMatchException(){
        super(ErrorCode.PASSWORD_MISS_MATCH);
    }

}