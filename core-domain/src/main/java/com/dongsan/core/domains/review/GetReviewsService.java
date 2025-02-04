package com.dongsan.core.domains.review;

import com.dongsan.core.domains.walkway.enums.ReviewSort;
import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.walkway.entity.Walkway;
import java.util.List;

public interface GetReviewsService {
    ReviewSort getSortType();

    List<Review> search(Integer size, Review review, Walkway walkway);
}
