package com.dongsan.domains.walkway.service.search.review;

import com.dongsan.domains.review.entity.Review;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.enums.ReviewSort;
import java.util.List;

public interface GetReviewsService {
    ReviewSort getSortType();

    List<Review> search(Integer size, Review review, Walkway walkway);
}
