package reminder.domain.museum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reminder.domain.card.controller.dto.CardResponse;
import reminder.domain.card.domain.CardCategory;
import reminder.domain.museum.controller.dto.MuseumResponse;
import reminder.domain.museum.domain.Museum;
import reminder.domain.museum.service.BannerUploadService;
import reminder.domain.museum.service.MuseumService;

import reminder.domain.museum.controller.dto.FollowingMuseumResponse;

import java.util.List;
import java.util.stream.Collectors;
import reminder.domain.card.domain.Card;

@Tag(name = "Museum")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/museums")
public class MuseumController {

    private final MuseumService museumService;
    private final BannerUploadService bannerUploadService;

    

    @Operation(summary = "팔로우한 사람들의 박물관 목록 조회")
    @GetMapping("/followings")
    public List<FollowingMuseumResponse> getFollowingMuseums() {
        return museumService.getFollowingMuseums();
    }

    @Operation(summary = "특정 유저 박물관의 카드 조회 (카테고리 필터링 가능)")
    @GetMapping("/{userId}/cards")
    public List<CardResponse> getCardsInMuseum(@PathVariable Long userId, @RequestParam(required = false) CardCategory category) {
        List<Card> cards = museumService.getCardsInUserMuseum(userId, category);
        return cards.stream()
                .map(card -> CardResponse.builder()
                        .id(card.getId())
                        .title(card.getTitle())
                        .content(card.getContent())
                        .imageUrl(card.getImageUrl())
                        .category(card.getCategory())
                        .userId(card.getUser().getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Operation(summary = "박물관 배너 이미지 업로드")
    @PostMapping("/{museumId}/banner")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadBanner(@PathVariable Long museumId, @RequestParam("file") MultipartFile file) {
        bannerUploadService.execute(file, museumId);
    }
}
