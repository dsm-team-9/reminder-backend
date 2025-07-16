package reminder.domain.pvp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import reminder.domain.pvp.Battle;
import reminder.domain.pvp.RoundResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class BattleDetailResponse {

    private Long battleId;
    private String initiatorUsername;
    private String opponentUsername;
    private int initiatorScore;
    private int opponentScore;
    private List<RoundResultResponse> roundResults;

    public static BattleDetailResponse from(Battle battle) {
        return BattleDetailResponse.builder()
                .battleId(battle.getId())
                .initiatorUsername(battle.getInitiatorUser().getName())
                .opponentUsername(battle.getOpponentUser().getName())
                .initiatorScore(battle.getInitiatorScore())
                .opponentScore(battle.getOpponentScore())
                .roundResults(battle.getRoundResults().stream()
                        .map(RoundResultResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RoundResultResponse {

        private int roundNumber;
        private String userCardName;
        private String opponentCardName;
        private boolean userWin;

        public static RoundResultResponse from(RoundResult roundResult) {
            return RoundResultResponse.builder()
                    .roundNumber(roundResult.getRoundNumber())
                    .userCardName(roundResult.getUserCard().getTitle())
                    .opponentCardName(roundResult.getOpponentCard().getTitle())
                    .userWin(roundResult.isUserWin())
                    .build();
        }
    }
}