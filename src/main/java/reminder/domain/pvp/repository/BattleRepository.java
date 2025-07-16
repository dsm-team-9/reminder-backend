package reminder.domain.pvp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reminder.domain.pvp.Battle;

public interface BattleRepository extends JpaRepository<Battle, Long> {
}
