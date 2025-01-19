package com.dongsan.domains.walkway.service.factory;

import com.dongsan.domains.walkway.enums.Sort;
import com.dongsan.domains.walkway.service.search.SearchWalkwayService;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SearchWalkwayServiceFactory {
    private final Map<Sort, SearchWalkwayService> walkwaySearchServiceMap = new EnumMap<>(Sort.class);

    public SearchWalkwayServiceFactory(List<SearchWalkwayService> walkwaySearchServices) {
        for (SearchWalkwayService walkwaySearchService : walkwaySearchServices) {
            this.walkwaySearchServiceMap.put(walkwaySearchService.getSortType(), walkwaySearchService);
        }
    }

    public SearchWalkwayService getService(Sort sort) {
        return walkwaySearchServiceMap.get(sort);
    }
}
