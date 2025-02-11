package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.domains.walkway.controller.dto.request.CreateReviewRequest;
import com.dongsan.domains.walkway.controller.dto.response.CreateReviewResponse;
import com.dongsan.domains.walkway.controller.dto.response.GetWalkwayRatingResponse;
import com.dongsan.domains.walkway.controller.dto.response.GetWalkwayReviewsResponse;
import com.dongsan.core.domains.review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/walkways")
@Tag(name = "산책로 리뷰")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성")
    @PostMapping("/{walkwayId}/review")
    public ResponseEntity<SuccessResponse<CreateReviewResponse>> createReview(
            @PathVariable Long walkwayId,
            @Validated @RequestBody CreateReviewRequest createReviewRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ResponseFactory.created(
                reviewService.createReview(customOAuth2User.getMemberId(), walkwayId, createReviewRequest));
    }

    @Operation(summary = "리뷰 내용 보기")
    @GetMapping("/{walkwayId}/review/content")
    public ResponseEntity<SuccessResponse<GetWalkwayReviewsResponse>> getWalkwayReviews(
            @PathVariable Long walkwayId,
            @RequestParam String sort,
            @RequestParam(required = false) Long lastId,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ResponseFactory.ok(reviewService.getWalkwayReviews(sort, lastId, walkwayId, size, customOAuth2User.getMemberId()));
    }

    @Operation(summary = "리뷰 별점 보기")
    @GetMapping("/{walkwayId}/review/rating")
    public ResponseEntity<SuccessResponse<GetWalkwayRatingResponse>> getWalkwaysRating(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        return ResponseFactory.ok(reviewService.getWalkwayRating(walkwayId, customOAuth2User.getMemberId()));
    }
}
