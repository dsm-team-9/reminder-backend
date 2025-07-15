package reminder.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reminder.domain.user.controller.dto.UserLoginRequest;
import reminder.domain.user.controller.dto.UserSignupRequest;
import reminder.domain.user.service.UserLoginService;
import reminder.domain.user.service.UserSignupService;
import reminder.global.security.jwt.TokenResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserSignupService userSignupService;
    private final UserLoginService userLoginService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public void userSignup(@RequestBody @Valid UserSignupRequest userSignupRequest) {
        userSignupService.execute(userSignupRequest);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public TokenResponse userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        return userLoginService.execute(userLoginRequest);
    }
}
