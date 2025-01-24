package com.dongsan.domains.walkway.service.search.walkway;

import com.dongsan.domains.walkway.dto.request.SearchWalkwayQuery;
import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
import com.dongsan.domains.walkway.enums.WalkwaySort;
import java.util.List;

public interface SearchWalkwayService {
    WalkwaySort getSortType();

    List<SearchWalkwayResult> search(SearchWalkwayQuery searchWalkwayRequest);
}
