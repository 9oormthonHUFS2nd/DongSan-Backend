package com.dongsan.core.domains.walkway.service.search.walkway;

import com.dongsan.core.domains.walkway.domain.Walkway;
import com.dongsan.core.domains.walkway.enums.WalkwaySort;
import java.util.List;

public interface SearchWalkway {
    WalkwaySort getSortType();

    List<Walkway> search(Long userId, Long lastId, Double latitude, Double longitude, Double distance, int size);
}
