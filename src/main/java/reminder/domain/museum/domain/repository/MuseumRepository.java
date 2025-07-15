package reminder.domain.museum.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reminder.domain.museum.domain.Museum;

import reminder.domain.user.entity.User;

import java.util.Optional;

public interface MuseumRepository extends JpaRepository<Museum, Long> {
    Optional<Museum> findByUser(User user);
}
