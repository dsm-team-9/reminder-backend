package reminder.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponseEntity {

    private int status;
    private String code;
    private String message;
    private LocalDateTime createLocalDateTime;

    public static ResponseEntity<ErrorResponseEntity> responseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .status(errorCode.getHttpStatus())
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .createLocalDateTime(LocalDateTime.now())
                        .build()
                );
    }
}
