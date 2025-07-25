package reminder.domain.pvp.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PvpCardSelectionRequest {

    private List<Long> cardIds;
}
