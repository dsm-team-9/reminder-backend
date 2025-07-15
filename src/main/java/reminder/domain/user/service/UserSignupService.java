package reminder.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reminder.domain.user.controller.dto.UserSignupRequest;
import reminder.domain.user.entity.repository.UserRepository;
import reminder.domain.user.entity.User;
import reminder.domain.user.exception.NicknameAlreadyExistsException;
import reminder.domain.user.exception.PhoneNumberAlreadyExistsException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void execute(UserSignupRequest request) {
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw PhoneNumberAlreadyExistsException.EXCEPTION;
        }
        if (userRepository.existsByName(request.getName())) {
            throw NicknameAlreadyExistsException.EXCEPTION;
        }

        User user = User.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }
}