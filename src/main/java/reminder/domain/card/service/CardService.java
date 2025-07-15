package reminder.domain.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reminder.domain.card.controller.dto.CardResponse;
import reminder.domain.card.domain.Card;
import reminder.domain.card.domain.CardCategory;
import reminder.domain.card.domain.repository.CardRepository;
import reminder.domain.user.entity.User;
import reminder.domain.user.facade.UserFacade;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserFacade userFacade;

    public List<CardResponse> getAllCards(CardCategory category) {
        User user = userFacade.getCurrentUser();
        List<Card> cards;
        if (category != null) {
            cards = cardRepository.findByUserAndCategory(user, category);
        } else {
            cards = cardRepository.findByUser(user);
        }
        return cards.stream()
                .map(card -> CardResponse.builder()
                        .id(card.getId())
                        .title(card.getTitle())
                        .content(card.getContent())
                        .imageUrl(card.getImageUrl())
                        .category(card.getCategory())
                        .userId(card.getUser().getId())
                        .museumId(card.getMuseum() != null ? card.getMuseum().getId() : null)
                        .build())
                .collect(Collectors.toList());
    }
}
