package com.dongsan.core.domains.walkway.service.factory;

import com.dongsan.core.domains.walkway.enums.ReviewSort;
import com.dongsan.core.domains.walkway.service.search.review.GetReviewsService;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GetReviewsServiceFactory {
    private final Map<ReviewSort, GetReviewsService> getReviewsServiceMap = new EnumMap<>(ReviewSort.class);

    public GetReviewsServiceFactory(List<GetReviewsService> getReviewsServices) {
        for (GetReviewsService getReviewsService : getReviewsServices) {
            this.getReviewsServiceMap.put(getReviewsService.getSortType(), getReviewsService);
        }
    }

    public GetReviewsService getService(ReviewSort sort) {
        return getReviewsServiceMap.get(sort);
    }
}
