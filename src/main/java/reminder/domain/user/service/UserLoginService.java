package reminder.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reminder.domain.user.controller.dto.UserLoginRequest;
import reminder.domain.user.entity.User;
import reminder.domain.user.entity.repository.UserRepository;
import reminder.domain.user.exception.PasswordMissMatchException;
import reminder.domain.user.exception.UserNotFoundException;
import reminder.global.security.jwt.JwtProvider;
import reminder.global.security.jwt.TokenResponse;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse execute(UserLoginRequest request) {
        //사용자 조회
        User user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(UserNotFoundException::new);
        //비밀번호 검증
        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new PasswordMissMatchException();
        }
        //JWT 토큰 생성
        //응답 DTO로 반환
        return jwtProvider.getToken(user);
    }
}
