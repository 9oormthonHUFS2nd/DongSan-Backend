package com.dongsan.domains.walkway.controller;

import com.dongsan.apiResponse.ResponseFactory;
import com.dongsan.apiResponse.SuccessResponse;
import com.dongsan.common.validation.annotation.ExistWalkway;
import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.walkway.dto.request.CreateReviewRequest;
import com.dongsan.domains.walkway.dto.response.CreateReviewResponse;
import com.dongsan.domains.walkway.usecase.WalkwayReviewUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/walkways")
@Tag(name = "💬산책로 리뷰", description = "Review")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    private final WalkwayReviewUseCase walkwayReviewUseCase;

    @Operation(summary = "리뷰 작성")
    @PostMapping("/{walkwayId}/review")
    public ResponseEntity<SuccessResponse<CreateReviewResponse>> createReview(
            @ExistWalkway @PathVariable Long walkwayId,
            @Validated @RequestBody CreateReviewRequest createReviewRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ResponseFactory.created(walkwayReviewUseCase.createReview(customOAuth2User.getMemberId(), walkwayId, createReviewRequest));
    }
}
