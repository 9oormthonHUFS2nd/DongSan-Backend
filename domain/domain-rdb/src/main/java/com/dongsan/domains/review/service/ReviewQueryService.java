package com.dongsan.domains.review.service;

import com.dongsan.domains.review.dto.RatingCount;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.review.repository.ReviewQueryDSLRepository;
import com.dongsan.domains.review.repository.ReviewRepository;
import com.dongsan.common.error.code.ReviewErrorCode;
import com.dongsan.common.error.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewQueryService {
    private final ReviewQueryDSLRepository reviewQueryDSLRepository;
    private final ReviewRepository reviewRepository;

    public Review getReview(Long reviewId){
        return reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));
    }

    public List<Review> getReviews(Integer limit, LocalDateTime lastCreatedAt, Long memberId) {
        return reviewQueryDSLRepository.getReviews(limit, lastCreatedAt, memberId);
    }

    public boolean existsByReviewId(Long reviewId){
        return reviewRepository.existsById(reviewId);
    }

    public List<Review> getWalkwayReviewsLatest(Integer limit, Long reviewId, Long walkwayId) {
        return reviewQueryDSLRepository.getWalkwayReviewsLatest(limit, reviewId, walkwayId);
    }

    public List<Review> getWalkwayReviewsRating(Integer limit, Long reviewId, Long walkwayId, Byte rating) {
        return reviewQueryDSLRepository.getWalkwayReviewsRating(limit, reviewId, walkwayId, rating);
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
}
