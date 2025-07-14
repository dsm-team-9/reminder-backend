package reminder.reminderbe.domain.user.exception;

import reminder.reminderbe.global.error.CustomException;
import reminder.reminderbe.global.error.ErrorCode;

public class PhoneNumberAlreadyExistsException extends CustomException{
    public static final CustomException EXCEPTION = new PhoneNumberAlreadyExistsException();

    private PhoneNumberAlreadyExistsException(){
        super(ErrorCode.ACCOUNTID_ALREADY_EXISTS);
    }
}