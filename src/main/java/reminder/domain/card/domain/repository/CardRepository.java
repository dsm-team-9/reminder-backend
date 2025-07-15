package reminder.domain.card.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reminder.domain.card.domain.Card;

import reminder.domain.card.domain.CardCategory;
import reminder.domain.museum.domain.Museum;
import reminder.domain.user.entity.User;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByUser(User user);
    List<Card> findByMuseum(Museum museum);
    List<Card> findByUserAndCategory(User user, CardCategory category);
    List<Card> findByMuseumAndCategory(Museum museum, CardCategory category);
}
