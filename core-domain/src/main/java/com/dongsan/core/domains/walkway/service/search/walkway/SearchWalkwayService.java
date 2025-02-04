package com.dongsan.core.domains.walkway.service.search.walkway;

import com.dongsan.core.domains.walkway.enums.WalkwaySort;
import com.dongsan.domains.walkway.dto.request.SearchWalkwayQuery;
import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
import java.util.List;

public interface SearchWalkwayService {
    WalkwaySort getSortType();

    List<SearchWalkwayResult> search(SearchWalkwayQuery searchWalkwayRequest);
}
