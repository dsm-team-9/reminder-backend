package reminder.domain.museum.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MuseumResponse {
    private Long id;
    private Long userId;
    private String bannerUrl;
}
