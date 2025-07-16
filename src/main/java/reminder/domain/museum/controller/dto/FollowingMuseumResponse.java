package reminder.domain.museum.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowingMuseumResponse {
    private final Long userId;
    private final String username;
    private final String bannerUrl;
    private final long cardCount;
}
