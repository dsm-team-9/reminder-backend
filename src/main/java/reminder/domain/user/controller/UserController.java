package reminder.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reminder.domain.user.controller.dto.request.UserSignupRequest;
import reminder.domain.user.service.UserSignupService;
import reminder.global.security.jwt.TokenResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserSignupService userSignupService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public TokenResponse login(@RequestBody UserSignupRequest request) {
        return userSignupService.signup(request);
    }
}
