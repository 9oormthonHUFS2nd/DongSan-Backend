package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import com.dongsan.api.domains.bookmark.BookmarksWithMarkedWalkwayResponse;
import com.dongsan.api.domains.image.ImageService;
import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.response.CreateWalkwayCourseImageRequest;
import com.dongsan.api.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.api.domains.walkway.dto.response.GetWalkwayResponse;
import com.dongsan.api.domains.walkway.dto.response.SearchWalkwayResponse;
import com.dongsan.api.domains.walkway.mapper.WalkwayMapper;
import com.dongsan.api.support.response.ApiResponse;
import com.dongsan.core.domains.bookmark.BookmarkService;
import com.dongsan.core.domains.image.Image;
import com.dongsan.core.domains.walkway.CreateWalkway;
import com.dongsan.core.domains.walkway.SearchWalkwayQuery;
import com.dongsan.core.domains.walkway.UpdateWalkway;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@Validated
public class WalkwayController {

    private final WalkwayService walkwayService;
    private final BookmarkService bookmarkService;
    private final ImageService s3UseCase;
    private final com.dongsan.core.domains.image.ImageService imageService;

    @Autowired
    public WalkwayController(WalkwayService walkwayService, BookmarkService bookmarkService, ImageService s3UseCase, com.dongsan.core.domains.image.ImageService imageService) {
        this.walkwayService = walkwayService;
        this.bookmarkService = bookmarkService;
        this.s3UseCase = s3UseCase;
        this.imageService = imageService;
    }

    @Operation(summary = "산책로 등록")
    @PostMapping("")
    public ApiResponse<CreateWalkwayResponse> createWalkway(
            @Validated @RequestBody CreateWalkwayRequest createWalkwayRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Image image = imageService.getImage(createWalkwayRequest.courseImageId());
        CreateWalkway createWalkway = WalkwayMapper.toCreateWalkway(createWalkwayRequest, image, customOAuth2User.getMemberId());
        Long walkwayId = walkwayService.createWalkway(createWalkway);
        return ApiResponse.success(new CreateWalkwayResponse(walkwayId));
    }

    @Operation(summary = "산책로 코스 이미지 등록")
    @PostMapping(value ="/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<CreateWalkwayCourseImageRequest> createWalkwayCourseImage(
            @RequestPart("courseImage") MultipartFile courseImage,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        String imageUrl = s3UseCase.uploadCourseImage(courseImage);
        Long imageId = imageService.createImage(imageUrl);
        return ApiResponse.success(new CreateWalkwayCourseImageRequest(imageId));
    }

    // todo : 북마크 여부 추가
    @Operation(summary = "산책로 단건 조회")
    @GetMapping("/{walkwayId}")
    public ApiResponse<GetWalkwayResponse> getWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Walkway walkway = walkwayService.getWalkway(walkwayId);
        boolean isLike = walkwayService.existsLikedWalkway(customOAuth2User.getMemberId(), walkwayId);
        return ApiResponse.success(new GetWalkwayResponse(walkway, isLike, false));
    }

    @Operation(summary = "북마크 목록 보기(산책로 마크 여부 포함)")
    @GetMapping("/{walkwayId}/bookmarks")
    public ApiResponse<BookmarksWithMarkedWalkwayResponse> getBookmarksWithMarkedWalkway(
            @PathVariable Long walkwayId,
            @RequestParam(required = false) Long lastId,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ApiResponse.success(
                bookmarkService.getBookmarksWithMarkedWalkway(customOAuth2User.getMemberId(), walkwayId, lastId, size));
    }
    @Operation(summary = "산책로 수정")
    @PutMapping("/{walkwayId}")
    public ApiResponse<Void> updateWalkway(
            @PathVariable Long walkwayId,
            @Validated @RequestBody UpdateWalkwayRequest updateWalkwayRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        UpdateWalkway updateWalkway = WalkwayMapper.toUpdateWalkway(updateWalkwayRequest, walkwayId);
        walkwayService.updateWalkway(updateWalkway, customOAuth2User.getMemberId());
        return ApiResponse.success(null);
    }

    @Operation(summary = "산책로 검색")
    @GetMapping("")
    public ApiResponse<SearchWalkwayResponse> searchWalkway(
            @RequestParam(name = "sort") String sort,
            @RequestParam(name = "latitude") Double latitude,
            @RequestParam(name = "longitude") Double longitude,
            @RequestParam(name = "distance") Double distance,
            @RequestParam(name = "lastId", required = false) Long lastId,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        SearchWalkwayQuery searchWalkwayQuery
                = new SearchWalkwayQuery(customOAuth2User.getMemberId(), longitude, latitude, distance, lastId, size);
        List<Walkway> walkways
                = walkwayService.searchWalkway(sort, searchWalkwayQuery);

        List<Long> walkwayIds = walkways.stream()
                .map(Walkway::walkwayId)
                .toList();

        Map<Long, Boolean> isLiked = walkwayService.existsLikedWalkways(customOAuth2User.getMemberId(), walkwayIds);
        return ApiResponse.success(new SearchWalkwayResponse(walkways, isLiked, size));
    }
}
