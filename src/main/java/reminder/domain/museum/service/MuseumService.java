package reminder.domain.museum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reminder.domain.card.domain.Card;
import reminder.domain.card.domain.CardCategory;
import reminder.domain.card.domain.repository.CardRepository;
import reminder.domain.museum.controller.dto.MuseumResponse;
import reminder.domain.museum.domain.Museum;
import reminder.domain.museum.domain.repository.MuseumRepository;
import reminder.domain.user.entity.User;
import reminder.domain.user.entity.repository.FriendshipRepository;
import reminder.domain.user.entity.repository.UserRepository;
import reminder.domain.user.exception.UserNotFoundException;

import reminder.domain.user.facade.UserFacade;
import reminder.domain.user.entity.Friendship;
import reminder.domain.museum.controller.dto.FollowingMuseumResponse;
import java.util.stream.Collectors;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MuseumService {

    private final MuseumRepository museumRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final UserFacade userFacade;
    private final FriendshipRepository friendshipRepository;

    

    public List<FollowingMuseumResponse> getFollowingMuseums() {
        User currentUser = userFacade.getCurrentUser();
        List<Friendship> followings = friendshipRepository.findByFollower(currentUser);

        return followings.stream()
                .map(Friendship::getFollowing)
                .map(followingUser -> {
                    Museum museum = museumRepository.findByUser(followingUser)
                            .orElseThrow(() -> new IllegalArgumentException("Museum not found for user: " + followingUser.getId()));
                    long cardCount = cardRepository.countByUser(followingUser);
                    return FollowingMuseumResponse.builder()
                            .userId(followingUser.getId())
                            .username(followingUser.getName())
                            .bannerUrl(museum.getBannerUrl())
                            .cardCount(cardCount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<Card> getCardsInMuseum(Long museumId, CardCategory category) {
        Museum museum = museumRepository.findById(museumId)
                .orElseThrow(() -> new IllegalArgumentException("Museum not found."));
        if (category != null) {
            return cardRepository.findByMuseumAndCategory(museum, category);
        } else {
            return cardRepository.findByMuseum(museum);
        }
    }

    public List<Card> getCardsInUserMuseum(Long userId, CardCategory category) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        Museum museum = museumRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Museum not found for this user."));
        return getCardsInMuseum(museum.getId(), category);
    }
}
