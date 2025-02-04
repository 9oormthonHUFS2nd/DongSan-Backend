package com.dongsan.core.domains.review;

import com.dongsan.core.domains.walkway.enums.ReviewSort;
import com.dongsan.core.domains.walkway.service.factory.GetReviewsServiceFactory;
import com.dongsan.domains.review.dto.RatingCount;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.review.repository.ReviewQueryDSLRepository;
import com.dongsan.domains.review.repository.ReviewRepository;
import com.dongsan.common.error.code.ReviewErrorCode;
import com.dongsan.common.error.exception.CustomException;
import com.dongsan.domains.walkway.entity.Walkway;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewReader {
    private final ReviewQueryDSLRepository reviewQueryDSLRepository;
    private final ReviewRepository reviewRepository;
    private final GetReviewsServiceFactory getReviewsServiceFactory;

    public Review getReview(Long reviewId){
        return reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));
    }

    public List<Review> getReviews(Integer limit, LocalDateTime lastCreatedAt, Long memberId) {
        return reviewQueryDSLRepository.getUserReviews(limit, lastCreatedAt, memberId);
    }

    public boolean existsByReviewId(Long reviewId){
        return reviewRepository.existsById(reviewId);
    }

    public List<RatingCount> getWalkwaysRating(Long walkwayId) {
        return reviewQueryDSLRepository.getWalkwaysRating(walkwayId);
    }

    public void isReviewOwner(Long reviewId, Long memberId){
        boolean result = reviewRepository.existsByIdAndMemberId(reviewId, memberId);
        if(!result){
            throw new CustomException(ReviewErrorCode.NOT_REVIEW_OWNER);
        }
    }

    public List<Review> getWalkwayReviews(Integer size, Review review, Walkway walkway, ReviewSort sort) {
        return getReviewsServiceFactory.getService(sort).search(size, review, walkway);
    }
}
