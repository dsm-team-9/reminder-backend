package reminder.domain.pvp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reminder.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Battle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_user_id")
    private User initiatorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_user_id")
    private User opponentUser;

    @Enumerated(EnumType.STRING)
    private BattleStatus status;

    @OneToMany(mappedBy = "battle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoundResult> roundResults = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "initiator_selected_cards", joinColumns = @JoinColumn(name = "battle_id"))
    @Column(name = "card_id")
    private List<Long> initiatorSelectedCardIds;

    @ElementCollection
    @CollectionTable(name = "opponent_selected_cards", joinColumns = @JoinColumn(name = "battle_id"))
    @Column(name = "card_id")
    private List<Long> opponentSelectedCardIds;

    private int currentRound;
    private int initiatorScore;
    private int opponentScore;

    public void addRoundResult(RoundResult roundResult) {
        roundResults.add(roundResult);
    }

    public void incrementRound() {
        this.currentRound++;
    }

    public void updateScore(boolean initiatorWins) {
        if (initiatorWins) {
            this.initiatorScore++;
        } else {
            this.opponentScore++;
        }
    }

    public void completeBattle() {
        this.status = BattleStatus.COMPLETED;
    }
}
