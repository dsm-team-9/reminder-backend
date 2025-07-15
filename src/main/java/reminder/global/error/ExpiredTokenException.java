package reminder.global.error;

public class ExpiredTokenException extends CustomException{

    public static final CustomException EXCEPTION = new ExpiredTokenException();

    private ExpiredTokenException() {
        super(ErrorCode.JWT_EXPIRED);
    }
}