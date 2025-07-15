package reminder.domain.user.exception;

import reminder.global.error.CustomException;
import reminder.global.error.ErrorCode;

public class NicknameAlreadyExistsException extends CustomException {
    public static final CustomException EXCEPTION = new NicknameAlreadyExistsException();

    private NicknameAlreadyExistsException(){
        super(ErrorCode.NICKNAME_ALREADY_EXISTS);
    }
}