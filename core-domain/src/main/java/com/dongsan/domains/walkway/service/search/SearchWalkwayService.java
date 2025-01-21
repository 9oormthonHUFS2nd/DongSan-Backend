package com.dongsan.domains.walkway.service.search;

import com.dongsan.domains.walkway.dto.request.SearchWalkwayQuery;
import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
import com.dongsan.domains.walkway.enums.Sort;
import java.util.List;

public interface SearchWalkwayService {
    Sort getSortType();

    List<SearchWalkwayResult> search(SearchWalkwayQuery searchWalkwayRequest);
}
