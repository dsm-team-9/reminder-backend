package reminder.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reminder.domain.user.controller.dto.UserLoginRequest;
import reminder.domain.user.controller.dto.UserSignupRequest;
import reminder.domain.user.controller.dto.UserResponse;
import reminder.domain.user.controller.dto.UserWithCardCountResponse;
import reminder.domain.user.entity.User;
import reminder.domain.user.facade.UserFacade;
import reminder.domain.user.service.FriendshipService;
import reminder.domain.user.service.UserLoginService;
import reminder.domain.user.service.UserSignupService;
import reminder.global.security.jwt.TokenResponse;
import reminder.global.security.principle.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Tag(name = "User")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserSignupService userSignupService;
    private final UserLoginService userLoginService;
    private final FriendshipService friendshipService;
    private final UserFacade userFacade;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void userSignup(@RequestBody @Valid UserSignupRequest userSignupRequest) {
        userSignupService.execute(userSignupRequest);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public TokenResponse userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        return userLoginService.execute(userLoginRequest);
    }

    @Operation(summary = "이름으로 사용자 검색")
    @GetMapping("/search")
    public List<UserWithCardCountResponse> searchUsersByName(@RequestParam String name) {
        return friendshipService.searchUsersByName(name);
    }

    @Operation(summary = "팔로우")
    @PostMapping("/{id}/follow")
    public void followUser(@PathVariable("id") Long followingId) {
        friendshipService.followUser(followingId);
    }

    @Operation(summary = "언팔로우")
    @DeleteMapping("/{id}/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollowUser(@PathVariable("id") Long followingId) {
        friendshipService.unfollowUser(followingId);
    }

    @Operation(summary = "팔로잉 목록 조회")
    @GetMapping("/following")
    public List<UserWithCardCountResponse> getFollowing() {
        return friendshipService.getFollowing();
    }

    @Operation(summary = "PVP 활성화/비활성화 설정")
    @PatchMapping("/pvp-status")
    public boolean updatePvpStatus(@RequestParam boolean enabled) {
        return friendshipService.updatePvpStatus(enabled);
    }

    @Operation(summary = "내 이름 조회")
    @GetMapping("/my-name")
    public String myName() {
        return userFacade.getCurrentUser().getName();
    }
}
