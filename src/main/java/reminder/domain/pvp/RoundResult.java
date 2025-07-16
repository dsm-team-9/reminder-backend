package reminder.domain.pvp;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reminder.domain.card.domain.Card;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoundResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_id")
    private Battle battle;

    private int roundNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_card_id")
    private Card userCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_card_id")
    private Card opponentCard;

    private boolean userWin;

    @Builder
    public RoundResult(Battle battle, int roundNumber, Card userCard, Card opponentCard, boolean userWin) {
        this.battle = battle;
        this.roundNumber = roundNumber;
        this.userCard = userCard;
        this.opponentCard = opponentCard;
        this.userWin = userWin;
    }
}