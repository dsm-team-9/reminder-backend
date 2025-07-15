package reminder.domain.user.controller.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

//import lombok.Getter;
//
//@Getter
public record UserSignupRequest(
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$", message = "이름은 2~20자 한글, 영문, 숫자만 사용할 수 있습니다.")
        String name,

        @Pattern(regexp = "^010\\d{8}$", message = "전화번호는 010으로 시작하는 11자리 숫자여야 합니다.")
        String phoneNumber,

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다.")
        String password
){}
