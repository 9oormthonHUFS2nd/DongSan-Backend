package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.api.domains.bookmark.BookmarksWithMarkedWalkwayResponse;
import com.dongsan.api.domains.image.S3UseCase;
import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.response.CreateWalkwayCourseImageRequest;
import com.dongsan.api.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.api.domains.walkway.dto.response.GetWalkwayResponse;
import com.dongsan.api.domains.walkway.dto.response.SearchWalkwayResponse;
import com.dongsan.api.domains.walkway.mapper.WalkwayMapper;
import com.dongsan.core.common.apiResponse.ResponseFactory;
import com.dongsan.core.common.apiResponse.SuccessResponse;
import com.dongsan.core.domains.bookmark.BookmarkService;
import com.dongsan.core.domains.image.Image;
import com.dongsan.core.domains.image.ImageService;
import com.dongsan.core.domains.walkway.domain.Walkway;
import com.dongsan.core.domains.walkway.usecase.HashtagService;
import com.dongsan.core.domains.walkway.usecase.WalkwayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
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

    private final WalkwayService walkwayService;
    private final BookmarkService bookmarkService;
    private final S3UseCase s3UseCase;
    private final ImageService imageService;

    @Operation(summary = "산책로 등록")
    @PostMapping("")
    public ResponseEntity<SuccessResponse<CreateWalkwayResponse>> createWalkway(
            @Validated @RequestBody CreateWalkwayRequest createWalkwayRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Image image = imageService.getImage(createWalkwayRequest.courseImageId());
        Walkway walkway = WalkwayMapper.toWalkway(createWalkwayRequest, image, customOAuth2User.getMemberId());
        return ResponseFactory.created(new CreateWalkwayResponse(walkwayService.createWalkway(walkway)));
    }

    @Operation(summary = "산책로 코스 이미지 등록")
    @PostMapping(value ="/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<CreateWalkwayCourseImageRequest>> createWalkwayCourseImage(
            @RequestPart("courseImage") MultipartFile courseImage,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        String imageUrl = s3UseCase.uploadCourseImage(courseImage);
        Image image = imageService.createImage(imageUrl);
        return ResponseFactory.created(new CreateWalkwayCourseImageRequest(image));
    }

    @Operation(summary = "산책로 단건 조회")
    @GetMapping("/{walkwayId}")
    public ResponseEntity<SuccessResponse<GetWalkwayResponse>> getWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Walkway walkway = walkwayService.getWalkway(walkwayId);
        boolean isLike = walkwayService.existsLikedWalkway(customOAuth2User.getMemberId(), walkwayId);
        return ResponseFactory.ok(new GetWalkwayResponse(walkway, isLike));
    }

    @Operation(summary = "북마크 목록 보기(산책로 마크 여부 포함)")
    @GetMapping("/{walkwayId}/bookmarks")
    public ResponseEntity<SuccessResponse<BookmarksWithMarkedWalkwayResponse>> getBookmarksWithMarkedWalkway(
            @PathVariable Long walkwayId,
            @RequestParam(required = false) Long lastId,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ResponseFactory.ok(
                bookmarkService.getBookmarksWithMarkedWalkway(customOAuth2User.getMemberId(), walkwayId, lastId, size));
    }
    @Operation(summary = "산책로 수정")
    @PutMapping("/{walkwayId}")
    public ResponseEntity<Void> updateWalkway(
            @PathVariable Long walkwayId,
            @Validated @RequestBody UpdateWalkwayRequest updateWalkwayRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Walkway updateWalkway = WalkwayMapper.toWalkway(updateWalkwayRequest, walkwayId, customOAuth2User.getMemberId());
        walkwayService.updateWalkway(updateWalkway);
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
        List<Walkway> walkways
                = walkwayService.searchWalkway(customOAuth2User.getMemberId(), sort, latitude, longitude, distance, lastId, size);

        List<Long> walkwayIds = walkways.stream()
                .map(Walkway::walkwayId)
                .toList();

        Map<Long, Boolean> isLiked = walkwayService.existsLikedWalkways(customOAuth2User.getMemberId(), walkwayIds);
        return ResponseFactory.ok(new SearchWalkwayResponse(walkways, isLiked, size));
    }
}
