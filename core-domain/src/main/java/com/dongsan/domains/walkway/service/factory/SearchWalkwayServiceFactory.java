package com.dongsan.domains.walkway.service.factory;

import com.dongsan.domains.walkway.enums.WalkwaySort;
import com.dongsan.domains.walkway.service.search.walkway.SearchWalkwayService;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SearchWalkwayServiceFactory {
    private final Map<WalkwaySort, SearchWalkwayService> walkwaySearchServiceMap = new EnumMap<>(WalkwaySort.class);

    public SearchWalkwayServiceFactory(List<SearchWalkwayService> walkwaySearchServices) {
        for (SearchWalkwayService walkwaySearchService : walkwaySearchServices) {
            this.walkwaySearchServiceMap.put(walkwaySearchService.getSortType(), walkwaySearchService);
        }
    }

    public SearchWalkwayService getService(WalkwaySort sort) {
        return walkwaySearchServiceMap.get(sort);
    }
}
