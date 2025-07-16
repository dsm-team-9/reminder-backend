package reminder.domain.pvp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CardPvpResultResponse {

    private Long cardId;
    private int winCount;
    private int loseCount;
    private double winRate;
    private String feedback;
}