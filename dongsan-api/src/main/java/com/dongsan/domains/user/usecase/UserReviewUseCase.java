package com.dongsan.domains.user.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.review.service.ReviewQueryService;
import com.dongsan.domains.user.dto.response.GetReview;
import com.dongsan.domains.user.mapper.UserReviewMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class UserReviewUseCase {
    private final ReviewQueryService reviewQueryService;

    @Transactional(readOnly = true)
    public GetReview getReviews(Integer limit, Long reviewId, Long memberId) {
        List<Review> reviews = reviewQueryService.getReviews(limit, reviewId, memberId);
        return UserReviewMapper.toGetReview(reviews);
    }
}
