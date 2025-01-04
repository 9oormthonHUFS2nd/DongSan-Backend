package com.dongsan.domains.user.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.walkway.service.ReviewQueryService;
import com.dongsan.domains.user.response.GetReviewResponse;
import com.dongsan.domains.user.mapper.UserReviewMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class UserReviewUseCase {
    private final ReviewQueryService reviewQueryService;

    @Transactional(readOnly = true)
    public GetReviewResponse getReviews(Integer limit, Long reviewId, Long memberId) {
        LocalDateTime lastCreatedAt = null;
        if(reviewId != null){
            // reviewId 검증
            // 1. 존재하는 reviewId 인지
            Review review = reviewQueryService.getReview(reviewId);
            // 2. 내가 작성한 review 인지 아닌지
            reviewQueryService.isReviewOwner(review.getId(), memberId);
            lastCreatedAt = review.getCreatedAt();
        }

        List<Review> reviews = reviewQueryService.getReviews(limit, lastCreatedAt, memberId);
        return UserReviewMapper.toGetReview(reviews);
    }


}
