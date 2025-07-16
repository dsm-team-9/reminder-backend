package reminder.domain.pvp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reminder.domain.pvp.controller.dto.BattleResponse;
import reminder.domain.pvp.service.PvpService;
import reminder.global.security.principle.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import reminder.domain.pvp.controller.dto.PvpCardSelectionRequest;

import java.util.List;

@Tag(name = "PVP")
@RestController
@RequestMapping("/api/pvp")
@RequiredArgsConstructor
public class PvpController {

    private final PvpService pvpService;

    @Operation(summary = "PVP 카드 선택")
    @PostMapping("/cards")
    public void selectPvpCards(@RequestBody PvpCardSelectionRequest request) {
        pvpService.updatePvpStatus(request.getCardIds(), true);
    }

    @Operation(summary = "PVP 활성화 여부 확인")
    @GetMapping("/active")
    public boolean isPvpActive() {
        return pvpService.isPvpActiveForUser();
    }

    @Operation(summary = "대결 시작 가능 여부 확인")
    @GetMapping("/can-initiate/{opponentUserId}")
    public boolean canInitiateBattle(@PathVariable Long opponentUserId) {
        return pvpService.canInitiateBattle(opponentUserId);
    }

    @Operation(summary = "대결 시작")
    @PostMapping("/initiate/{opponentUserId}")
    public BattleResponse initiateBattle(@PathVariable Long opponentUserId) {
        return pvpService.initiateBattle(opponentUserId);
    }

    @Operation(summary = "카드 선택")
    @PostMapping("/{battleId}/select-cards")
    public BattleResponse selectCardsForBattle(@PathVariable Long battleId, @RequestBody List<Long> cardIds) {
        return pvpService.selectCardsForBattle(battleId, cardIds);
    }

    @Operation(summary = "라운드 진행")
    @PostMapping("/{battleId}/play-round")
    public BattleResponse playRound(@PathVariable Long battleId, @RequestBody Long userSelectedCardId) {
        return pvpService.playRound(battleId, userSelectedCardId);
    }

    @Operation(summary = "대결 결과 조회")
    @GetMapping("/{battleId}/result")
    public BattleResponse getBattleResult(@PathVariable Long battleId) {
        return pvpService.getBattleResult(battleId);
    }
}
