package com.dongsan.domains.review.service;

import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.review.repository.ReviewQueryDSLRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewQueryService {
    private final ReviewQueryDSLRepository reviewQueryDSLRepository;

    public List<Review> getReviews(Integer limit, Integer reviewId, Long memberId) {
        return reviewQueryDSLRepository.getReviews(limit, reviewId, memberId);
    }
}
