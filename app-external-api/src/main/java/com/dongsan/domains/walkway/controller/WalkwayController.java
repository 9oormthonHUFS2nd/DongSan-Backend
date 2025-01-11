package com.dongsan.domains.walkway.controller;

import com.dongsan.common.apiResponse.ResponseFactory;
import com.dongsan.common.apiResponse.SuccessResponse;
import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.bookmark.dto.response.BookmarksWithMarkedWalkwayResponse;
import com.dongsan.domains.bookmark.usecase.BookmarkUseCase;
import com.dongsan.domains.dev.usecase.DevUseCase;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayCourseRequest;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwaySearchResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayWithLikedResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.usecase.HashtagUseCase;
import com.dongsan.domains.walkway.usecase.LikedWalkwayUseCase;
import com.dongsan.domains.walkway.usecase.WalkwayUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/walkways")
@Tag(name = "🚶🏻‍산책로", description = "Walkway")
@RequiredArgsConstructor
@Validated
public class WalkwayController {

    private final WalkwayUseCase walkwayUseCase;
    private final BookmarkUseCase bookmarkUseCase;
    private final LikedWalkwayUseCase likedWalkwayUseCase;
    private final HashtagUseCase hashtagUseCase;
    private final DevUseCase devUseCase;

    @Operation(summary = "산책로 등록")
    @PostMapping("")
    public ResponseEntity<SuccessResponse<CreateWalkwayResponse>> createWalkway(
            @Validated @RequestBody CreateWalkwayRequest createWalkwayRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Walkway walkway = walkwayUseCase.createWalkway(createWalkwayRequest, customOAuth2User.getMemberId());
        hashtagUseCase.createHashtagWalkways(walkway, createWalkwayRequest.hashtags());
        return ResponseFactory.created(new CreateWalkwayResponse(walkway));
    }

    @Operation(summary = "산책로 코스 등록")
    @PostMapping("/{walkwayId}/course")
    public ResponseEntity<SuccessResponse<CreateWalkwayResponse>> createWalkwayCourse(
            @PathVariable Long walkwayId,
            @RequestBody CreateWalkwayCourseRequest createWalkwayCourseRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Walkway walkway = walkwayUseCase.createWalkwayCourse(createWalkwayCourseRequest, customOAuth2User.getMemberId(), walkwayId);
        return ResponseFactory.created(new CreateWalkwayResponse(walkway));
    }

    @Operation(summary = "산책로 코스 이미지 등록")
    @PostMapping(value ="/{walkwayId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<CreateWalkwayResponse>> createWalkwayCourseImage(
            @PathVariable Long walkwayId,
            @RequestPart("courseImage") MultipartFile courseImage,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        String imageUrl;
        try {
            imageUrl = devUseCase.uploadImage(courseImage);
        } catch (Exception e) {
            imageUrl = "";
        }
        Walkway walkway = walkwayUseCase.createWalkwayCourseImageUrl(imageUrl, customOAuth2User.getMemberId(), walkwayId);
        return ResponseFactory.created(new CreateWalkwayResponse(walkway));
    }

    @Operation(summary = "산책로 단건 조회")
    @GetMapping("/{walkwayId}")
    public ResponseEntity<SuccessResponse<GetWalkwayWithLikedResponse>> getWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Walkway walkway = walkwayUseCase.getWalkwayWithLiked(walkwayId, customOAuth2User.getMemberId());
        return ResponseFactory.ok(new GetWalkwayWithLikedResponse(walkway));
    }

    @Operation(summary = "산책로 검색")
    @GetMapping("")
    public ResponseEntity<SuccessResponse<GetWalkwaySearchResponse>> getWalkwaysSearch(
            @RequestParam(name = "type") String type,
            @RequestParam(name = "hashtags", defaultValue = "") String hashtags,
            @RequestParam(name = "latitude") Double latitude,
            @RequestParam(name = "longitude") Double longitude,
            @RequestParam(name = "distance") Double distance,
            @RequestParam(name = "lastId", required = false) Long lastId,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        List<Walkway> walkways = walkwayUseCase.getWalkwaysSearch(customOAuth2User.getMemberId(), type, latitude, longitude, distance, hashtags, lastId, size);
        List<Boolean> likedWalkways = likedWalkwayUseCase.existsLikedWalkways(customOAuth2User.getMemberId(), walkways);
        return ResponseFactory.ok(new GetWalkwaySearchResponse(walkways, likedWalkways, size));
    }

    @Operation(summary = "북마크 목록 보기(산책로 마크 여부 포함)")
    @GetMapping("/{walkwayId}/bookmarks")
    public ResponseEntity<SuccessResponse<BookmarksWithMarkedWalkwayResponse>> getBookmarksWithMarkedWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ResponseFactory.ok(bookmarkUseCase.getBookmarksWithMarkedWalkway(customOAuth2User.getMemberId(), walkwayId));
    }
    @Operation(summary = "산책로 수정")
    @PutMapping("/{walkwayId}")
    public ResponseEntity<Void> updateWalkway(
            @PathVariable Long walkwayId,
            @Validated @RequestBody UpdateWalkwayRequest updateWalkwayRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        walkwayUseCase.updateWalkway(updateWalkwayRequest, customOAuth2User.getMemberId(), walkwayId);
        return ResponseFactory.noContent();
    }
}
