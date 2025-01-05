package com.dongsan.domains.user.controller;

import com.dongsan.common.apiResponse.ResponseFactory;
import com.dongsan.common.apiResponse.SuccessResponse;
import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.user.response.GetReviewResponse;
import com.dongsan.domains.user.usecase.UserReviewUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/reviews")
@Tag(name = "💬 리뷰", description = "Review")
@RequiredArgsConstructor
@Validated
public class UserReviewController {
    private final UserReviewUseCase userReviewUsecase;

    /**
     * 작성한 리뷰 전체 보기
     */
    @Operation(summary = "내가 작성한 리뷰 보기")
    @GetMapping()
    public ResponseEntity<SuccessResponse<GetReviewResponse>> getReviews(
            @RequestParam(defaultValue = "5") Integer limit,
            @RequestParam(required = false) Long reviewId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        GetReviewResponse response = userReviewUsecase.getReviews(limit, reviewId, customOAuth2User.getMemberId());
        return ResponseFactory.ok(response);
    }

}
