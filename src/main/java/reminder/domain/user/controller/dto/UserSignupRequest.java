package reminder.domain.user.controller.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserSignupRequest(
        @NotBlank(message = "이릉은 필수입니다.")
        String name,

        @NotBlank(message = "전화번호는 필수입니다.")
        @Length(min = 11, max =  11)
        String phoneNumber,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
){}
