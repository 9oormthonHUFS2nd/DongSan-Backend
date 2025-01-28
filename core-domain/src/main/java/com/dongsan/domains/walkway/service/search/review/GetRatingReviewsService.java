package com.dongsan.domains.walkway.service.search.review;

import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.review.repository.ReviewQueryDSLRepository;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.ReviewSort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetRatingReviewsService implements GetReviewsService{

    private final ReviewQueryDSLRepository reviewQueryDSLRepository;

    @Override
    public ReviewSort getSortType() {
        return ReviewSort.RATING;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> search(Integer size, Review review, Walkway walkway) {
        return reviewQueryDSLRepository.getWalkwayReviewsRating(size, walkway.getId(), review);
    }
}
