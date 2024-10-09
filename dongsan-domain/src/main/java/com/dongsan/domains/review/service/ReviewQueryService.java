package com.dongsan.domains.review.service;

import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.review.repository.ReviewQueryDSLRepository;
import com.dongsan.domains.review.repository.ReviewRepository;
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

    public List<Review> getReviews(Integer limit, Long reviewId, Long memberId) {
        return reviewQueryDSLRepository.getReviews(limit, reviewId, memberId);
    }

    public boolean existsByReviewId(Long reviewId){
        return reviewRepository.existsById(reviewId);
    }
}
