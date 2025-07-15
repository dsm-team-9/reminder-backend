package reminder.global.error.handler;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reminder.global.error.CustomException;
import reminder.global.error.ErrorCode;
import reminder.global.error.ErrorResponseEntity;

import org.springframework.validation.BindException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException customException) {

        return ErrorResponseEntity.responseEntity(customException.getErrorCode());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseEntity> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ErrorResponseEntity.responseEntity(ErrorCode.INVALID_INPUT, message);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponseEntity> handleBindException(BindException ex) {
        String message = ex.getFieldError().getDefaultMessage();
        return ErrorResponseEntity.responseEntity(ErrorCode.INVALID_INPUT, message);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseEntity> handleUnexpectedException(Exception ex) {
        return ErrorResponseEntity.responseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}