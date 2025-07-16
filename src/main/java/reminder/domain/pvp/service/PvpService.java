package reminder.domain.pvp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reminder.domain.card.domain.Card;
import reminder.domain.card.domain.repository.CardRepository;
import reminder.domain.card.service.CardOverallService;
import reminder.domain.pvp.controller.dto.CardPvpResultResponse;
import reminder.domain.pvp.controller.dto.PvpCardSelectionRequest;
import reminder.domain.user.entity.User;
import reminder.domain.user.entity.repository.UserRepository;
import reminder.domain.user.exception.UserNotFoundException;
import reminder.domain.user.facade.UserFacade;
import reminder.domain.pvp.Battle;
import reminder.domain.pvp.BattleStatus;
import reminder.domain.pvp.repository.BattleRepository;
import reminder.domain.pvp.controller.dto.BattleResponse;

import reminder.domain.pvp.RoundResult;
import reminder.domain.pvp.repository.RoundResultRepository;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import reminder.domain.pvp.controller.dto.GameResultResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PvpService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final BattleRepository battleRepository;
    private final RoundResultRepository roundResultRepository;
    private final CardOverallService cardOverallService;
    private final UserFacade userFacade;

    private static final int MIN_CARDS_FOR_PVP = 5;
    private static final int CARDS_PER_BATTLE = 5;
    private static final int TOTAL_ROUNDS = 5;

    public boolean isPvpActiveForUser() {
        User user = userFacade.getCurrentUser();
        return user.isPvpEnabled() && cardRepository.countByUserAndIsPvpCard(user, true) >= MIN_CARDS_FOR_PVP;
    }

    @Transactional
    public void updatePvpStatus(PvpCardSelectionRequest request, boolean enabled) {
        User user = userFacade.getCurrentUser();

        if (enabled) {
            if (request.getCardIds().size() != MIN_CARDS_FOR_PVP) {
                throw new IllegalStateException("You must select exactly " + MIN_CARDS_FOR_PVP + " cards to enable PVP.");
            }
            List<Card> userCards = cardRepository.findByUser(user);
            userCards.forEach(card -> card.updateIsPvpCard(false));

            List<Card> selectedCards = cardRepository.findAllById(request.getCardIds());
            if (selectedCards.size() != MIN_CARDS_FOR_PVP ) {
                throw new IllegalStateException("Invalid card selection.");
            }
            System.out.println("asfjgasfkhjlglaksfhj");

            selectedCards.forEach(card -> card.updateIsPvpCard(true));
        }

        user.updatePvpEnabled(enabled);
    }

    public boolean canInitiateBattle(Long opponentUserId) {
        User initiatorUser = userFacade.getCurrentUser();
        User opponentUser = userRepository.findById(opponentUserId).orElse(null);

        if (opponentUser == null) {
            return false;
        }

        if (!initiatorUser.isPvpEnabled()) {
            return false;
        }
        if (cardRepository.countByUserAndIsPvpCard(initiatorUser, true) < MIN_CARDS_FOR_PVP) {
            return false;
        }
        if (!opponentUser.isPvpEnabled()) {
            return false;
        }
        if (cardRepository.countByUserAndIsPvpCard(opponentUser, true) < MIN_CARDS_FOR_PVP) {
            return false;
        }
        return true; // Can initiate battle
    }

    @Transactional
    public BattleResponse initiateBattle(Long opponentUserId) {
        if (!canInitiateBattle(opponentUserId)) {
            throw new IllegalStateException("Cannot initiate battle");
        }

        User initiatorUser = userFacade.getCurrentUser();
        User opponentUser = userRepository.findById(opponentUserId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        // Automatically select cards for the battle
        List<Card> initiatorPvpCards = cardRepository.findByUserAndIsPvpCard(initiatorUser, true);
        List<Long> initiatorCardIds = initiatorPvpCards.stream().map(Card::getId).collect(Collectors.toList());

        List<Card> opponentPvpCards = cardRepository.findByUserAndIsPvpCard(opponentUser, true);
        List<Long> opponentCardIds = opponentPvpCards.stream().map(Card::getId).collect(Collectors.toList());

        // Calculate and set overall for selected cards
        for (Card card : initiatorPvpCards) {
            int overall = cardOverallService.calculateOverall(card.getContent());
            card.setOverall(overall);
            cardRepository.save(card);
        }
        for (Card card : opponentPvpCards) {
            int overall = cardOverallService.calculateOverall(card.getContent());
            card.setOverall(overall);
            cardRepository.save(card);
        }

        Battle battle = Battle.builder()
                .initiatorUser(initiatorUser)
                .opponentUser(opponentUser)
                .status(BattleStatus.IN_PROGRESS) // Start immediately
                .currentRound(0)
                .initiatorScore(0)
                .opponentScore(0)
                .initiatorSelectedCardIds(initiatorCardIds)
                .opponentSelectedCardIds(opponentCardIds)
                .build();

        battleRepository.save(battle);
        return new BattleResponse(battle);
    }

    @Transactional
    public BattleResponse playRound(Long battleId, Long userSelectedCardId) {
        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new IllegalArgumentException("Battle not found."));

        if (battle.getStatus() != BattleStatus.IN_PROGRESS) {
            throw new IllegalStateException("Battle is not in progress.");
        }
        if (battle.getCurrentRound() >= TOTAL_ROUNDS) {
            throw new IllegalStateException("All rounds have been played.");
        }

        // User's card
        Card userCard = cardRepository.findById(userSelectedCardId)
                .orElseThrow(() -> new IllegalArgumentException("User selected card not found."));

        // AI's card (randomly selected from opponent's selected cards)
        List<Long> opponentSelectedCardIds = battle.getOpponentSelectedCardIds();
        if (opponentSelectedCardIds == null || opponentSelectedCardIds.isEmpty()) {
            throw new IllegalStateException("Opponent has not selected cards yet.");
        }
        Long aiSelectedCardId = opponentSelectedCardIds.get(new Random().nextInt(opponentSelectedCardIds.size()));
        Card aiCard = cardRepository.findById(aiSelectedCardId)
                .orElseThrow(() -> new IllegalArgumentException("AI selected card not found."));

        // Compare overall and determine winner
        boolean initiatorWinsRound = userCard.getOverall() > aiCard.getOverall();
        battle.updateScore(initiatorWinsRound);
        battle.incrementRound();

        RoundResult roundResult = RoundResult.builder()
                .battle(battle)
                .roundNumber(battle.getCurrentRound())
                .userCard(userCard)
                .opponentCard(aiCard)
                .userWin(initiatorWinsRound)
                .build();
        roundResultRepository.save(roundResult);
        battle.addRoundResult(roundResult);

        if (battle.getCurrentRound() == TOTAL_ROUNDS) {
            battle.completeBattle();
            // Handle battle results (trophy, feedback)
            if (battle.getInitiatorScore() > battle.getOpponentScore()) {
                // Initiator wins: copy opponent's card
                Card trophyCard = Card.builder()
                        .title(aiCard.getTitle())
                        .content(aiCard.getContent())
                        .imageUrl(aiCard.getImageUrl())
                        .overall(aiCard.getOverall())
                        .category(aiCard.getCategory())
                        .user(battle.getInitiatorUser())
                        .build();
                cardRepository.save(trophyCard);
            } else if (battle.getInitiatorScore() < battle.getOpponentScore()) {
                // Initiator loses: provide feedback
                String feedback = cardOverallService.provideFeedbackForCard(userCard.getTitle(), userCard.getContent(), userCard.getOverall());
                System.out.println("Feedback for losing card: " + feedback);
            }
        }
        battleRepository.save(battle);
        return new BattleResponse(battle);
    }

    public GameResultResponse getLatestGameResult() {
        User user = userFacade.getCurrentUser();
        Battle latestBattle = battleRepository.findTopByInitiatorUserAndStatusOrderByIdDesc(user, BattleStatus.COMPLETED)
                .orElseThrow(() -> new IllegalStateException("No completed battles found."));

        return GameResultResponse.of(latestBattle, cardOverallService);
    }
}