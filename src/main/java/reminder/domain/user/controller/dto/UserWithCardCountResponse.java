package reminder.domain.user.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithCardCountResponse {
    private Long id;
    private String name;
    private String phoneNumber;
    private long cardCount;
}
