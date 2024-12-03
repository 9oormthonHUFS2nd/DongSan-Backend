package com.dongsan.domains.walkway.controller;

import com.dongsan.apiResponse.ResponseFactory;
import com.dongsan.apiResponse.SuccessResponse;
import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.walkway.usecase.LikedWalkwayUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/walkways")
@Tag(name = "💬산책로 좋아요", description = "LikedWalkway")
@RequiredArgsConstructor
@Validated
public class LikedWalkwayController {
    private final LikedWalkwayUseCase likedWalkwayUseCase;

    @Operation(summary = "산책로 좋아요")
    @PostMapping("/{walkwayId}/likes")
    public ResponseEntity<SuccessResponse<Object>> createLikedWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        likedWalkwayUseCase.createLikedWalkway(customOAuth2User.getMemberId(), walkwayId);
        return ResponseFactory.created(null);
    }

    @Operation(summary = "산책로 좋아요 취소")
    @DeleteMapping("/{walkwayId}/likes")
    public ResponseEntity<SuccessResponse<Object>> deleteLikedWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        likedWalkwayUseCase.deleteLikedWalkway(customOAuth2User.getMemberId(), walkwayId);
        return ResponseFactory.ok(null);
    }
}
