package reminder.domain.user.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reminder.domain.user.entity.Friendship;
import reminder.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByFollowerAndFollowing(User follower, User following);
    List<Friendship> findByFollower(User follower);
    List<Friendship> findByFollowing(User following);
}
