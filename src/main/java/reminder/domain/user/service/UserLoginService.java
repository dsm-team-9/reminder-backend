package reminder.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reminder.domain.user.controller.dto.UserLoginRequest;
import reminder.domain.user.entity.repository.UserRepository;
import reminder.global.security.jwt.JwtProvider;
import reminder.global.security.jwt.TokenResponse;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    public void execute(UserLoginRequest request) {
    }
}
