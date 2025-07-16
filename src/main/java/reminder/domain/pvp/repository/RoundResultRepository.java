package reminder.domain.pvp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reminder.domain.pvp.RoundResult;

import java.util.List;

public interface RoundResultRepository extends JpaRepository<RoundResult, Long> {

    List<RoundResult> findByBattleId(Long battleId);

    @Query("SELECT r FROM RoundResult r WHERE r.userCard.id = :cardId OR r.opponentCard.id = :cardId")
    List<RoundResult> findByCardId(@Param("cardId") Long cardId);
}