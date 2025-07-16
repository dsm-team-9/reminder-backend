package reminder.domain.pvp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reminder.domain.pvp.Battle;
import reminder.domain.pvp.BattleStatus;
import reminder.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface BattleRepository extends JpaRepository<Battle, Long> {

    @Query("SELECT b FROM Battle b WHERE b.initiatorUser = :user OR b.opponentUser = :user")
    List<Battle> findByUser(@Param("user") User user);

    Optional<Battle> findTopByInitiatorUserAndStatusOrderByIdDesc(User user, BattleStatus status);
}
