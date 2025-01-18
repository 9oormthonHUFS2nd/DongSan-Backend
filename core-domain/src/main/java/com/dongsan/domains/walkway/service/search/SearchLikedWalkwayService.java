package com.dongsan.domains.walkway.service.search;

import com.dongsan.domains.walkway.dto.request.SearchWalkwayQuery;
import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
import com.dongsan.domains.walkway.enums.Sort;
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
    public Sort getSortType() {
        return Sort.LIKED;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchWalkwayResult> search(SearchWalkwayQuery searchWalkwayRequest) {
        return walkwayQueryDSLRepository.searchWalkwaysLiked(searchWalkwayRequest);
    }
}
