package reminder.reminderbe.domain.user.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reminder.reminderbe.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
}
