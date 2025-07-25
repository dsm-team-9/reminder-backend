package reminder.global.security.principle;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import reminder.domain.user.entity.User;
import reminder.domain.user.entity.repository.UserRepository;
import reminder.domain.user.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        return new AuthDetails(user);
    }
}