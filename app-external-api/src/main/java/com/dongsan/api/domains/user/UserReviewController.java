package com.dongsan.api.domains.user;

import com.dongsan.api.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.dongsan.core.common.apiResponse.ResponseFactory;
import com.dongsan.core.common.apiResponse.SuccessResponse;
import com.dongsan.api.domains.review.GetReviewResponse;
import com.dongsan.core.domains.review.UserReviewService;
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
@Tag(name = "마이페이지")
@RequiredArgsConstructor
@Validated
public class UserReviewController {
    private final UserReviewService userReviewUsecase;

    /**
     * 작성한 리뷰 전체 보기
     */
    @Operation(summary = "내가 작성한 리뷰 보기")
    @GetMapping()
    public ResponseEntity<SuccessResponse<GetReviewResponse>> getReviews(
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        GetReviewResponse response = userReviewUsecase.getReviews(size, lastId, customOAuth2User.getMemberId());
        return ResponseFactory.ok(response);
    }

}
