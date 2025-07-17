package reminder.domain.card.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reminder.domain.card.controller.dto.CardCreateRequest;
import reminder.domain.card.controller.dto.CardChatRequest;
import reminder.domain.card.controller.dto.CardChatResponse;
import reminder.domain.card.controller.dto.CardResponse;
import reminder.domain.card.controller.dto.CardUpdateRequest;
import reminder.domain.card.service.CreateCardService;
import reminder.domain.card.service.CardService;
import reminder.domain.card.service.CardChatService;
import reminder.domain.card.service.CardIntroductionService;
import reminder.domain.card.domain.CardCategory;
import reminder.global.security.principle.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Tag(name = "Card")
@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {

    private final CreateCardService createCardService;
    private final CardService cardService;
    private final CardChatService cardChatService;
    private final CardIntroductionService cardIntroductionService;

    @Operation(summary = "카드 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCard(@RequestBody CardCreateRequest request) {
        createCardService.createCard(request);
    }

    @Operation(summary = "내 카드 조회 (카테고리 필터링 가능)")
    @GetMapping
    public List<CardResponse> getAllCards(@AuthenticationPrincipal AuthDetails authDetails, @RequestParam(required = false) CardCategory category) {
        return cardService.getAllCards(category);
    }

    @Operation(summary = "카드와 대화")
    @PostMapping("/{cardId}/chat")
    public CardChatResponse chatWithCard(@PathVariable Long cardId, @RequestBody CardChatRequest request) {
        return cardChatService.chatWithCard(cardId, request.getMessage());
    }

    @Operation(summary = "카드 내용 수정")
    @PatchMapping("/{cardId}")
    public void updateCardContent(@PathVariable Long cardId, @RequestBody CardUpdateRequest request) {
        cardService.updateCardContent(cardId, request);
    }

    @Operation(summary = "카드 자기소개")
    @GetMapping("/{cardId}/introduction")
    public CardChatResponse getCardIntroduction(@PathVariable Long cardId) {
        return cardIntroductionService.getCardIntroduction(cardId);
    }
}
