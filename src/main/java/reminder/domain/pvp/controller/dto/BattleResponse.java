package reminder.domain.pvp.controller.dto;

import lombok.Builder;
import lombok.Getter;
import reminder.domain.pvp.Battle;
import reminder.domain.pvp.BattleStatus;

@Getter
public class BattleResponse {

    private final Long id;
    private final Long initiatorUserId;
    private final Long opponentUserId;
    private final BattleStatus status;
    private final int currentRound;
    private final int initiatorScore;
    private final int opponentScore;

    @Builder
    public BattleResponse(Battle battle) {
        this.id = battle.getId();
        this.initiatorUserId = battle.getInitiatorUser().getId();
        this.opponentUserId = battle.getOpponentUser().getId();
        this.status = battle.getStatus();
        this.currentRound = battle.getCurrentRound();
        this.initiatorScore = battle.getInitiatorScore();
        this.opponentScore = battle.getOpponentScore();
    }
}
