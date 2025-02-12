package com.dongsan.core.domains.review;

import com.dongsan.domains.review.entity.Review;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UserReviewService {
    private final ReviewReader reviewReader;

    @Transactional(readOnly = true)
    public GetReviewResponse getReviews(Integer size, Long reviewId, Long memberId) {
        LocalDateTime lastCreatedAt = null;
        if(reviewId != null){
            // reviewId 검증
            // 1. 존재하는 reviewId 인지
            Review review = reviewReader.getReview(reviewId);
            // 2. 내가 작성한 review 인지 아닌지
            reviewReader.isReviewOwner(review.getId(), memberId);
            lastCreatedAt = review.getCreatedAt();
        }
        List<Review> reviews = reviewReader.getReviews(size+1, lastCreatedAt, memberId);
        boolean hasNext = reviews.size() > size;
        if(hasNext){
            reviews.remove(reviews.size()-1);
        }
        return GetReviewResponse.from(reviews, hasNext);
    }


}
