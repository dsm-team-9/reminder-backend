package reminder.domain.user.exception;

import reminder.global.error.CustomException;
import reminder.global.error.ErrorCode;

public class PhoneNumberAlreadyExistsException extends CustomException{
    public static final CustomException EXCEPTION = new PhoneNumberAlreadyExistsException();

    private PhoneNumberAlreadyExistsException(){
        super(ErrorCode.ACCOUNTID_ALREADY_EXISTS);
    }
}