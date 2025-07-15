package reminder.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reminder.domain.user.controller.dto.request.UserSignupRequest;
import reminder.domain.user.entity.User;
import reminder.domain.user.entity.repository.UserRepository;
import reminder.global.security.jwt.JwtProvider;
import reminder.global.security.jwt.TokenResponse;

@Service
@RequiredArgsConstructor
public class UserSignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public TokenResponse signup(UserSignupRequest request) {

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber()).orElseThrow(() -> new IllegalArgumentException("asdf"));

        return jwtProvider.getToken(user);
    }
}
