package com.dongsan.api.domains.user;

import com.dongsan.api.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.bookmark.controller.GetBookmarksResponse;
import com.dongsan.core.domains.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "마이페이지")
public class UserProfileController {

    private final MemberService userProfileUsecase;

    /**
     * 유저 프로필 조회
     */
    @Operation(summary = "사용자 프로필 조회")
    @GetMapping("/users/profile")
    public ResponseEntity<SuccessResponse<GetProfileResponse>> getProfile(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ResponseFactory.ok(userProfileUsecase.getUserProfile(customOAuth2User.getMemberId()));
    }

    /**
     * 유저 북마크 리스트 조회
     */
    @Operation(summary = "북마크한 산책로 제목 리스트 보기")
    @GetMapping("/users/bookmarks/title")
    public ResponseEntity<SuccessResponse<GetBookmarksResponse>> getBookmarks(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        GetBookmarksResponse response = userProfileUsecase.getUserBookmarks(customOAuth2User.getMemberId(), lastId, size);
        return ResponseFactory.ok(response);
    }
}
