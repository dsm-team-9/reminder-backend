package reminder.domain.card.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reminder.domain.card.domain.CardCategory;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private CardCategory category;
    private Long userId;
    private Long museumId;
}
