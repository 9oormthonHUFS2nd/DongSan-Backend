package com.dongsan.domains.user.controller;

import com.dongsan.apiResponse.ResponseFactory;
import com.dongsan.domains.user.usecase.UserProfileUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO: 마이페이지로 통합
@RestController
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileUsecase userProfileUsecase;

    @GetMapping("/users/profile")
    public ResponseEntity<?> getUserProfile() {
        return ResponseFactory.ok(userProfileUsecase.getUserProfile(1L));
    }

    @GetMapping("/users/bookmarks/title")
    public ResponseEntity<?> getUserBookmarks(@RequestParam(defaultValue = "0") Long bookmarkId, @RequestParam(defaultValue = "10") Integer size) {
        return ResponseFactory.ok(userProfileUsecase.getUserBookmarks(1L, bookmarkId, size));
    }
}
