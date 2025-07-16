package reminder.domain.pvp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import reminder.domain.card.domain.Card;
import reminder.domain.card.service.CardOverallService;
import reminder.domain.pvp.Battle;
import reminder.domain.pvp.RoundResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class GameResultResponse {

    private int totalWins;
    private int totalLosses;
    private double winRate;
    private List<RoundDetailResponse> roundResults;

    public static GameResultResponse of(Battle battle, CardOverallService cardOverallService) {
        int totalWins = battle.getInitiatorScore();
        int totalLosses = battle.getOpponentScore();
        double winRate = (totalWins + totalLosses) == 0 ? 0 : (double) totalWins / (totalWins + totalLosses) * 100;

        List<RoundDetailResponse> roundDetails = battle.getRoundResults().stream()
                .map(roundResult -> RoundDetailResponse.of(roundResult, cardOverallService))
                .collect(Collectors.toList());

        return GameResultResponse.builder()
                .totalWins(totalWins)
                .totalLosses(totalLosses)
                .winRate(winRate)
                .roundResults(roundDetails)
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RoundDetailResponse {
        private int roundNumber;
        private String userCardName;
        private String result;
        private String feedback;

        public static RoundDetailResponse of(RoundResult roundResult, CardOverallService cardOverallService) {
            boolean userWon = roundResult.isUserWin();
            String feedback = null;
            if (!userWon) {
                Card losingCard = roundResult.getUserCard();
                feedback = cardOverallService.provideFeedbackForCard(losingCard.getTitle(), losingCard.getContent(), losingCard.getOverall());
            }

            return RoundDetailResponse.builder()
                    .roundNumber(roundResult.getRoundNumber())
                    .userCardName(roundResult.getUserCard().getTitle())
                    .result(userWon ? "승" : "패")
                    .feedback(feedback)
                    .build();
        }
    }
}