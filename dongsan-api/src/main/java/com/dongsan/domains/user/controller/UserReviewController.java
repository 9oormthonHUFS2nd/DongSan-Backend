package com.dongsan.domains.user.controller;

import com.dongsan.apiResponse.ResponseFactory;
import com.dongsan.apiResponse.SuccessResponse;
import com.dongsan.domains.user.dto.response.GetReview;
import com.dongsan.domains.user.usecase.UserReviewUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/users/reviews")
@RequiredArgsConstructor
public class UserReviewController {
    private final UserReviewUseCase userReviewUsecase;

    /**
     * 작성한 리뷰 전체 보기
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<GetReview>> getReviews(
            @RequestParam(defaultValue = "5") Integer limit,
            @RequestParam Integer reviewId  // TODO 검증 어노테이션 생성
    ){
        Long memberId = 1L;  // (하드 코딩 수정)
        GetReview response = userReviewUsecase.getReviews(limit, reviewId, memberId);
        return ResponseFactory.ok(response);
    }

}
