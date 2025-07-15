package reminder.domain.card.controller.dto;

import lombok.Getter;
import lombok.Setter;

import reminder.domain.card.domain.CardCategory;

@Getter
@Setter
public class CardCreateRequest {
    private String title;
    private String content;
    private CardCategory category;
}
