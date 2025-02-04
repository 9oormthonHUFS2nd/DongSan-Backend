package com.dongsan.core.domains.walkway.service.search.walkway;

import com.dongsan.core.domains.walkway.enums.WalkwaySort;
import com.dongsan.domains.walkway.dto.request.SearchWalkwayQuery;
import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
import com.dongsan.domains.walkway.repository.WalkwayQueryDSLRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchLikedWalkwayService implements SearchWalkwayService {

    private final WalkwayQueryDSLRepository walkwayQueryDSLRepository;

    @Override
    public WalkwaySort getSortType() {
        return WalkwaySort.LIKED;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchWalkwayResult> search(SearchWalkwayQuery searchWalkwayRequest) {
        return walkwayQueryDSLRepository.searchWalkwaysLiked(searchWalkwayRequest);
    }
}
