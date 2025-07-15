package reminder.domain.museum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reminder.domain.card.controller.dto.CardResponse;
import reminder.domain.card.domain.CardCategory;
import reminder.domain.museum.controller.dto.MuseumResponse;
import reminder.domain.museum.domain.Museum;
import reminder.domain.museum.service.MuseumService;

import java.util.List;
import java.util.stream.Collectors;
import reminder.domain.card.domain.Card;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/museums")
public class MuseumController {

    private final MuseumService museumService;

    @GetMapping("/{userId}")
    public MuseumResponse getMuseumByUserId(@PathVariable Long userId) {
        return museumService.getMuseumByUserId(userId);
    }

    @GetMapping("/{userId}/cards")
    public List<CardResponse> getCardsInMuseum(@PathVariable Long userId, @RequestParam(required = false) CardCategory category) {
        MuseumResponse museum = museumService.getMuseumByUserId(userId);
        List<Card> cards = museumService.getCardsInMuseum(museum.getId(), category);
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
