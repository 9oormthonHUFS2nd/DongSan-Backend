package com.dongsan.core.domains.walkway.service.search.walkway;

import com.dongsan.core.domains.walkway.SearchWalkwayQuery;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.enums.WalkwaySort;
import java.util.List;

public interface SearchWalkway {
    WalkwaySort getSortType();

    List<Walkway> search(SearchWalkwayQuery searchWalkwayQuery);
}
