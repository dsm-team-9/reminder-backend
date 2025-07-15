package reminder.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reminder.domain.user.controller.dto.UserSignupRequest;
import reminder.domain.user.entity.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserSignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // di 추가 x
    // execute 메소드 내부 변경하기
    public void execute(UserSignupRequest request) {

    }
}
