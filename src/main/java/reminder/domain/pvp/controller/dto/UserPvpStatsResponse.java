package reminder.domain.pvp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserPvpStatsResponse {

    private int totalWins;
    private int totalLosses;
    private int totalGames;
    private double winRate;
}