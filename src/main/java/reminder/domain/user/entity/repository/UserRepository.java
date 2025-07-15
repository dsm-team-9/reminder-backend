package reminder.domain.user.entity.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import reminder.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<Object> findByName(String name);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByName(String name);
}
