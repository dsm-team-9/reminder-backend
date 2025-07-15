package reminder.domain.user.controller.dto;

import lombok.Getter;

@Getter
public class UserSignupRequest{
        private String name;
        private String phoneNumber;
        private String password;
}
