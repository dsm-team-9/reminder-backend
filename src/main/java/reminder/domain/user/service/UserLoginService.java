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
    // di 추가 x
    // execute 메소드 내부 내용 변경하기, 함수 리턴타입 변경 힌트: 토큰 반환
    public void execute(UserLoginRequest request) {
    }
}
