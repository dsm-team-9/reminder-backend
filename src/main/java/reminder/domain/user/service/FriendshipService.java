package reminder.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reminder.domain.user.controller.dto.UserResponse;
import reminder.domain.user.entity.Friendship;
import reminder.domain.user.entity.User;
import reminder.domain.user.entity.repository.FriendshipRepository;
import reminder.domain.user.facade.UserFacade;
import reminder.domain.user.entity.repository.UserRepository;
import reminder.domain.user.exception.UserNotFoundException;
import reminder.domain.user.controller.dto.UserWithCardCountResponse;
import reminder.domain.card.domain.repository.CardRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendshipService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserFacade userFacade;
    private final CardRepository cardRepository;

    public List<UserWithCardCountResponse> searchUsersByName(String name) {
        return userRepository.findByNameContaining(name).stream()
                .map(user -> {
                    long cardCount = cardRepository.countByUser(user);
                    return UserWithCardCountResponse.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .phoneNumber(user.getPhoneNumber())
                            .cardCount(cardCount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void followUser(Long followingId) {
        User follower = userFacade.getCurrentUser();
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (follower.equals(following)) {
            throw new IllegalArgumentException("Cannot follow yourself.");
        }

        Optional<Friendship> existingFollow = friendshipRepository.findByFollowerAndFollowing(follower, following);
        if (existingFollow.isPresent()) {
            throw new IllegalStateException("Already following this user.");
        }

        Friendship friendship = Friendship.builder()
                .follower(follower)
                .following(following)
                .build();
        friendshipRepository.save(friendship);
    }

    @Transactional
    public void unfollowUser(Long followingId) {
        User follower = userFacade.getCurrentUser();
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        Friendship friendship = friendshipRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new IllegalArgumentException("Not following this user."));
        friendshipRepository.delete(friendship);
    }

    public List<UserWithCardCountResponse> getFollowing() {
        User user = userFacade.getCurrentUser();
        return friendshipRepository.findByFollower(user).stream()
                .map(friendship -> {
                    User followedUser = friendship.getFollowing();
                    long cardCount = cardRepository.countByUser(followedUser);
                    return UserWithCardCountResponse.builder()
                            .id(followedUser.getId())
                            .name(followedUser.getName())
                            .phoneNumber(followedUser.getPhoneNumber())
                            .cardCount(cardCount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean updatePvpStatus(boolean enabled) {
        User user = userFacade.getCurrentUser();

        if (enabled) {
            long cardCount = cardRepository.countByUser(user);
            if (cardCount < 5) {
                throw new IllegalStateException("PVP cannot be enabled. You need at least 5 cards.");
            }
        }
        user.updatePvpEnabled(enabled);
        userRepository.save(user);
        return user.isPvpEnabled();
    }
}
