package com.dongsan.api.domains.bookmark;

import com.dongsan.api.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.core.common.apiResponse.ResponseFactory;
import com.dongsan.core.common.apiResponse.SuccessResponse;
import com.dongsan.core.common.validation.annotation.ExistBookmark;
import com.dongsan.core.common.validation.annotation.ExistWalkway;
import com.dongsan.core.domains.bookmark.GetBookmarkDetailParam;
import com.dongsan.core.domains.bookmark.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
@Tag(name = "북마크")
@Validated
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping()
    @Operation(summary = "북마크 생성")
    public ResponseEntity<SuccessResponse<BookmarkIdResponse>> createBookmark(
            @Valid @RequestBody BookmarkNameRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        BookmarkIdResponse response = bookmarkService.createBookmark(customOAuth2User.getMemberId(), request);
        return ResponseFactory.created(response);
    }

    @PutMapping("/{bookmarkId}")
    @Operation(summary = "북마크 이름 변경")
    public ResponseEntity<Void> renameBookmark(
            @ExistBookmark @PathVariable Long bookmarkId,
            @Valid @RequestBody BookmarkNameRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        bookmarkService.renameBookmark(customOAuth2User.getMemberId(), bookmarkId, request);
        return ResponseFactory.noContent();
    }

    @PostMapping("/{bookmarkId}/walkways")
    @Operation(summary = "북마크에 산책로를 추가")
    public ResponseEntity<Void> addWalkway(
            @ExistBookmark @PathVariable Long bookmarkId,
            @Valid @RequestBody WalkwayIdRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        bookmarkService.addWalkway(customOAuth2User.getMemberId(), bookmarkId, request);
        return ResponseFactory.noContent();
    }

    @DeleteMapping("/{bookmarkId}/walkways/{walkwayId}")
    @Operation(summary = "북마크에 산책로를 제거")
    public ResponseEntity<Void> deleteWalkway(
            @ExistBookmark @PathVariable Long bookmarkId,
            @ExistWalkway @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        bookmarkService.deleteWalkway(customOAuth2User.getMemberId(), bookmarkId, walkwayId);
        return ResponseFactory.noContent();
    }

    @DeleteMapping("/{bookmarkId}")
    @Operation(summary = "북마크 삭제")
    public ResponseEntity<Void> deleteBookmark(
            @ExistBookmark @PathVariable Long bookmarkId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        bookmarkService.deleteBookmark(customOAuth2User.getMemberId(), bookmarkId);
        return ResponseFactory.noContent();
    }

    /**
     * 북마크에 추가된 시간을 기준으로 내림차순 조회
     */
    @GetMapping("/{bookmarkId}/walkways")
    @Operation(summary = "북마크 상세 조회")
    public ResponseEntity<SuccessResponse<GetBookmarkDetailResponse>> getBookmarkDetail(
            @ExistBookmark @PathVariable Long bookmarkId,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        GetBookmarkDetailResponse response = bookmarkService.getBookmarkDetails(new GetBookmarkDetailParam(
                customOAuth2User.getMemberId(), bookmarkId, size, lastId));
        return ResponseFactory.ok(response);
    }
}
