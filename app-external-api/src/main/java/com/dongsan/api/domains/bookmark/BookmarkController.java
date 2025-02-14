package com.dongsan.api.domains.bookmark;

import com.dongsan.api.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.api.support.response.ApiResponse;
import com.dongsan.api.support.validation.annotation.ExistBookmark;
import com.dongsan.api.support.validation.annotation.ExistWalkway;
import com.dongsan.core.domains.bookmark.BookmarkService;
import com.dongsan.core.domains.common.CursorPagingRequest;
import com.dongsan.core.domains.common.CursorPagingResponse;
import com.dongsan.core.domains.walkway.Walkway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "북마크")
@Validated
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/bookmarks")
    @Operation(summary = "북마크 생성")
    public ApiResponse<BookmarkIdResponse> createBookmark(
            @Valid @RequestBody BookmarkNameRequest dto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        Long response = bookmarkService.createBookmark(customOAuth2User.getMemberId(), dto.name());
        return ApiResponse.success(new BookmarkIdResponse(response));
    }

    @PutMapping("/bookmarks/{bookmarkId}")
    @Operation(summary = "북마크 이름 변경")
    public ApiResponse<Void> renameBookmark(
            @PathVariable Long bookmarkId,
            @Valid @RequestBody BookmarkNameRequest dto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        bookmarkService.renameBookmark(customOAuth2User.getMemberId(), bookmarkId, dto.name());
        return ApiResponse.success();
    }

    @PostMapping("/bookmarks/{bookmarkId}/walkways")
    @Operation(summary = "북마크에 산책로를 추가")
    public ApiResponse<Void> addWalkway(
            @PathVariable Long bookmarkId,
            @Valid @RequestBody WalkwayIdRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        bookmarkService.includeWalkway(customOAuth2User.getMemberId(), bookmarkId, request.walkwayId());
        return ApiResponse.success();
    }

    @DeleteMapping("/bookmarks/{bookmarkId}/walkways/{walkwayId}")
    @Operation(summary = "북마크에 산책로를 제거")
    public ApiResponse<Void> deleteWalkway(
            @ExistBookmark @PathVariable Long bookmarkId,
            @ExistWalkway @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        bookmarkService.excludeWalkway(customOAuth2User.getMemberId(), bookmarkId, walkwayId);
        return ApiResponse.success();
    }

    @DeleteMapping("/bookmarks/{bookmarkId}")
    @Operation(summary = "북마크 삭제")
    public ApiResponse<Void> deleteBookmark(
            @PathVariable Long bookmarkId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        bookmarkService.deleteBookmark(customOAuth2User.getMemberId(), bookmarkId);
        return ApiResponse.success();
    }


    // TODO : 이거 머지되면, name 필드 제외하도록 수정 요청
    @GetMapping("/bookmarks/{bookmarkId}/walkways")
    @Operation(summary = "북마크 상세 조회")
    public ApiResponse<GetBookmarkDetailResponse> getBookmarkDetail(
            @ExistBookmark @PathVariable Long bookmarkId,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        CursorPagingResponse<Walkway> pagingResponse = bookmarkService.getBookmarkDetails(customOAuth2User.getMemberId(), bookmarkId, new CursorPagingRequest(lastId, size)));
        return ApiResponse.success(new GetBookmarkDetailResponse(pagingResponse));
    }


    @Operation(summary = "사용자가 북마크한 산책로 제목 리스트 보기")
    @GetMapping("/users/bookmarks/title")
    public ApiResponse<GetBookmarksResponse> getBookmarks(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        GetBookmarksResponse response = bookmarkService.getUserBookmarks(customOAuth2User.getMemberId(), new CursorPagingRequest(lastId, size));
        return ApiResponse.success(response);
    }
}
