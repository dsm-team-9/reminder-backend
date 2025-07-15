package reminder.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reminder.domain.user.controller.dto.UserLoginRequest;
import reminder.domain.user.controller.dto.UserSignupRequest;
import reminder.domain.user.service.UserLoginService;
import reminder.domain.user.service.UserSignupService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserSignupService userSignupService;
    private final UserLoginService userLoginService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public void userSignup(UserSignupRequest userSignupRequest) {
        userSignupService.execute(userSignupRequest);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public void userLogin(UserLoginRequest userLoginRequest) {
        userLoginService.execute(userLoginRequest);
    }
}
