package reminder.domain.museum.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import reminder.domain.card.domain.Card;
import reminder.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Museum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "museum")
    private List<Card> card = new ArrayList<>();

    private String bannerUrl;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void updateBanner(String url) {
        this.bannerUrl = url;
    }
}
