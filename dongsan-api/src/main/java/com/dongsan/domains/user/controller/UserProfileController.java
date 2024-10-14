package com.dongsan.domains.user.controller;

import com.dongsan.apiResponse.ResponseFactory;
import com.dongsan.apiResponse.SuccessResponse;
import com.dongsan.domains.user.dto.response.GetBookmarksResponse;
import com.dongsan.domains.user.dto.response.GetProfileResponse;
import com.dongsan.domains.user.usecase.UserProfileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileUseCase userProfileUsecase;

    /**
     * 유저 프로필 조회
     */
    @GetMapping("/users/profile")
    public ResponseEntity<SuccessResponse<GetProfileResponse>> getProfile() {
        return ResponseFactory.ok(userProfileUsecase.getUserProfile(1L));
    }

    /**
     * 유저 북마크 리스트 조회
     */
    @GetMapping("/users/bookmarks/title")
    public ResponseEntity<SuccessResponse<GetBookmarksResponse>> getBookmarks(
            @RequestParam(required = false) Long bookmarkId,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        return ResponseFactory.ok(userProfileUsecase.getUserBookmarks(1L, bookmarkId, limit));
    }
}
