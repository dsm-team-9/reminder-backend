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

    public List<UserResponse> searchUsersByName(String name) {
        return userRepository.findByNameContaining(name).stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .phoneNumber(user.getPhoneNumber())
                        .build())
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

    public List<UserResponse> getFollowing() {
        User user = userFacade.getCurrentUser();
        return friendshipRepository.findByFollower(user).stream()
                .map(friendship -> UserResponse.builder()
                        .id(friendship.getFollowing().getId())
                        .name(friendship.getFollowing().getName())
                        .phoneNumber(friendship.getFollowing().getPhoneNumber())
                        .build())
                .collect(Collectors.toList());
    }
}
