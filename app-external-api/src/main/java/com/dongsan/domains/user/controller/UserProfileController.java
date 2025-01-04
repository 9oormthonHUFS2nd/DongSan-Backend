package com.dongsan.domains.user.controller;

import com.dongsan.common.apiResponse.ResponseFactory;
import com.dongsan.common.apiResponse.SuccessResponse;
import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.user.response.GetBookmarksResponse;
import com.dongsan.domains.user.response.GetProfileResponse;
import com.dongsan.domains.user.usecase.UserProfileUseCase;
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
@Tag(name = "🤳🏻 사용자 프로필 & 북마크", description = "User Profile & Bookmark")
public class UserProfileController {

    private final UserProfileUseCase userProfileUsecase;

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
            @RequestParam(required = false) Long bookmarkId,
            @RequestParam(defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        GetBookmarksResponse response = userProfileUsecase.getUserBookmarks(customOAuth2User.getMemberId(), bookmarkId, size);
        return ResponseFactory.ok(response);
    }
}
