package com.dongsan.core.domains.walkway.service.search.walkway;

import com.dongsan.core.domains.walkway.SearchWalkwayQuery;
import com.dongsan.core.domains.walkway.WalkwayRepository;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.enums.WalkwaySort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchWalkwayRating implements SearchWalkway {
    private final WalkwayRepository walkwayRepository;

    public SearchWalkwayRating(WalkwayRepository walkwayRepository) {
        this.walkwayRepository = walkwayRepository;
    }

    @Override
    public WalkwaySort getSortType() {
        return WalkwaySort.RATING;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Walkway> search(SearchWalkwayQuery searchWalkwayQuery) {
        return walkwayRepository.searchWalkwaysRating(searchWalkwayQuery);
    }
}
