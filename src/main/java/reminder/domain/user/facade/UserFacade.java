package reminder.domain.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reminder.domain.user.entity.User;
import reminder.domain.user.entity.repository.UserRepository;
import reminder.domain.user.exception.UserNotFoundException;


@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByAccountId(accountId);
    }

    private User getUserByAccountId(String accountId) {
        return userRepository.findByPhoneNumber(accountId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }
}
