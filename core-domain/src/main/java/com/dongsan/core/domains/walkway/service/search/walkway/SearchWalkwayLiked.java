package com.dongsan.core.domains.walkway.service.search.walkway;

import com.dongsan.core.domains.walkway.WalkwayRepository;
import com.dongsan.core.domains.walkway.domain.Walkway;
import com.dongsan.core.domains.walkway.enums.WalkwaySort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchWalkwayLiked implements SearchWalkway {

    private final WalkwayRepository walkwayRepository;

    @Override
    public WalkwaySort getSortType() {
        return WalkwaySort.LIKED;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Walkway> search(Long userId, Long lastId, Double latitude, Double longitude, Double distance, int size) {
        return walkwayRepository.searchWalkwaysLiked(userId, lastId, longitude, latitude, distance, size);
    }
}
