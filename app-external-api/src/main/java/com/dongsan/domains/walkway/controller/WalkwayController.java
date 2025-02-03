package com.dongsan.domains.walkway.controller;

import com.dongsan.common.apiResponse.ResponseFactory;
import com.dongsan.common.apiResponse.SuccessResponse;
import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.bookmark.dto.response.BookmarksWithMarkedWalkwayResponse;
import com.dongsan.domains.bookmark.usecase.BookmarkUseCase;
import com.dongsan.domains.image.entity.Image;
import com.dongsan.domains.image.usecase.ImageUseCase;
import com.dongsan.domains.image.usecase.S3UseCase;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayHistoryRequest;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayCourseImageRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayHistoryResponse;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayHistoriesResponse;
import com.dongsan.domains.walkway.dto.response.GetWalkwayWithLikedResponse;
import com.dongsan.domains.walkway.dto.response.SearchWalkwayResponse;
import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.entity.WalkwayHistory;
import com.dongsan.domains.walkway.usecase.HashtagUseCase;
import com.dongsan.domains.walkway.usecase.WalkwayHistoryUseCase;
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
@Tag(name = "산책로")
@RequiredArgsConstructor
@Validated
public class WalkwayController {

    private final WalkwayUseCase walkwayUseCase;
    private final BookmarkUseCase bookmarkUseCase;
    private final HashtagUseCase hashtagUseCase;
    private final S3UseCase s3UseCase;
    private final ImageUseCase imageUseCase;
    private final WalkwayHistoryUseCase walkwayHistoryUseCase;

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

    @Operation(summary = "산책로 코스 이미지 등록")
    @PostMapping(value ="/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<CreateWalkwayCourseImageRequest>> createWalkwayCourseImage(
            @RequestPart("courseImage") MultipartFile courseImage,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        String imageUrl = s3UseCase.uploadCourseImage(courseImage);
        Image image = imageUseCase.createImage(imageUrl);
        return ResponseFactory.created(new CreateWalkwayCourseImageRequest(image));
    }

    @Operation(summary = "산책로 단건 조회")
    @GetMapping("/{walkwayId}")
    public ResponseEntity<SuccessResponse<GetWalkwayWithLikedResponse>> getWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Walkway walkway = walkwayUseCase.getWalkwayWithLiked(walkwayId, customOAuth2User.getMemberId());
        boolean isMarked = walkwayUseCase.isMarkedWalkway(walkwayId, customOAuth2User.getMemberId());
        return ResponseFactory.ok(new GetWalkwayWithLikedResponse(walkway, isMarked));
    }

    @Operation(summary = "북마크 목록 보기(산책로 마크 여부 포함)")
    @GetMapping("/{walkwayId}/bookmarks")
    public ResponseEntity<SuccessResponse<BookmarksWithMarkedWalkwayResponse>> getBookmarksWithMarkedWalkway(
            @PathVariable Long walkwayId,
            @RequestParam(required = false) Long lastId,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ResponseFactory.ok(bookmarkUseCase.getBookmarksWithMarkedWalkway(customOAuth2User.getMemberId(), walkwayId, lastId, size));
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

    @Operation(summary = "산책로 검색")
    @GetMapping("")
    public ResponseEntity<SuccessResponse<SearchWalkwayResponse>> searchWalkway(
            @RequestParam(name = "sort") String sort,
            @RequestParam(name = "latitude") Double latitude,
            @RequestParam(name = "longitude") Double longitude,
            @RequestParam(name = "distance") Double distance,
            @RequestParam(name = "lastId", required = false) Long lastId,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        List<SearchWalkwayResult> searchWalkwayResults
                = walkwayUseCase.searchWalkway(customOAuth2User.getMemberId(), sort, latitude, longitude, distance, lastId, size);
        return ResponseFactory.ok(new SearchWalkwayResponse(searchWalkwayResults, size));
    }

    @Operation(summary = "산책로 이용 기록")
    @PostMapping("/{walkwayId}/history")
    public ResponseEntity<SuccessResponse<CreateWalkwayHistoryResponse>> createHistory(
            @PathVariable Long walkwayId,
            @Validated @RequestBody CreateWalkwayHistoryRequest createWalkwayHistoryRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Long walkwayHistoryId
                = walkwayHistoryUseCase.createWalkwayHistory(customOAuth2User.getMemberId(), walkwayId, createWalkwayHistoryRequest);
        return ResponseFactory.created(new CreateWalkwayHistoryResponse(walkwayHistoryId));
    }

    @Operation(summary = "리뷰 작성 가능한 산책로 이용 기록 보기")
    @GetMapping("/{walkwayId}/history")
    public ResponseEntity<SuccessResponse<GetWalkwayHistoriesResponse>> getHistories(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        List<WalkwayHistory> walkwayHistories
                = walkwayHistoryUseCase.getWalkwayHistories(customOAuth2User.getMemberId(), walkwayId);

        return ResponseFactory.ok(GetWalkwayHistoriesResponse.from(walkwayHistories));
    }
}
