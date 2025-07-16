package reminder.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reminder.domain.card.domain.Card;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;
    private String password;
    private boolean pvpEnabled = true;
    @OneToMany(mappedBy = "user")
    private List<Card> feeds = new ArrayList<>();

    public void updatePvpEnabled(boolean enabled) {
        this.pvpEnabled = enabled;
    }
}
