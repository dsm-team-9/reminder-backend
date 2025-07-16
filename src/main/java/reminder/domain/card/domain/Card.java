package reminder.domain.card.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import reminder.domain.museum.domain.Museum;
import reminder.domain.user.entity.User;
import reminder.domain.card.domain.CardCategory;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private int overall = 0;
    @Enumerated(EnumType.STRING)
    private CardCategory category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "museum_id")
    private Museum museum;

    private boolean isPvpCard = false;

    public void updateContent(String content) {
        this.content = content;
    }

    public void setOverall(int overall) {
        this.overall = overall;
    }

    public void updateIsPvpCard(boolean isPvpCard) {
        this.isPvpCard = isPvpCard;
    }
}
