package com.dongsan.domains.user.controller;

import com.dongsan.apiResponse.ResponseFactory;
import com.dongsan.domains.user.usecase.UserProfileUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/profile")
public class UserProfileController {

    private final UserProfileUsecase userProfileUsecase;

    @GetMapping("")
    public ResponseEntity<?> getUserProfile() {
        return ResponseFactory.ok(userProfileUsecase.getUserProfile(1L));
    }
}
