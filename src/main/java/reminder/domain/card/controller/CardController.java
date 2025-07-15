package reminder.domain.card.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reminder.domain.card.controller.dto.CardCreateRequest;
import reminder.domain.card.controller.dto.CardChatRequest;
import reminder.domain.card.controller.dto.CardChatResponse;
import reminder.domain.card.controller.dto.CardResponse;
import reminder.domain.card.service.CreateCardService;
import reminder.domain.card.service.CardService;
import reminder.domain.card.service.CardChatService;
import reminder.domain.card.domain.CardCategory;
import reminder.global.security.principle.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CreateCardService createCardService;
    private final CardService cardService;
    private final CardChatService cardChatService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCard(@RequestBody CardCreateRequest request, @AuthenticationPrincipal AuthDetails authDetails) {
        createCardService.createCard(request, authDetails.getUser().getId());
    }

    @GetMapping
    public List<CardResponse> getAllCards(@AuthenticationPrincipal AuthDetails authDetails, @RequestParam(required = false) CardCategory category) {
        return cardService.getAllCards(category);
    }

    @PostMapping("/{cardId}/chat")
    public CardChatResponse chatWithCard(@PathVariable Long cardId, @RequestBody CardChatRequest request) {
        return cardChatService.chatWithCard(cardId, request.getMessage());
    }
}
