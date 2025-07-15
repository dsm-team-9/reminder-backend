package reminder.global.error.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reminder.global.error.CustomException;
import reminder.global.error.ErrorResponseEntity;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException customException) {

        return ErrorResponseEntity.responseEntity(customException.getErrorCode());
    }
}