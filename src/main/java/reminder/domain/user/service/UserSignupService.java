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
import reminder.domain.museum.domain.Museum;
import reminder.domain.museum.domain.repository.MuseumRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MuseumRepository museumRepository;

    public void execute(UserSignupRequest request) {
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw PhoneNumberAlreadyExistsException.EXCEPTION;
        }
        if (userRepository.existsByName(request.name())) {
            throw NicknameAlreadyExistsException.EXCEPTION;
        }

        User user = User.builder()
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);

        Museum museum = Museum.builder()
                .user(user)
                .build();
        museumRepository.save(museum);
    }
}