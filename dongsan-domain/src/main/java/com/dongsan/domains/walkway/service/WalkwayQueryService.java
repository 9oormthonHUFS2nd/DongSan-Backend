package com.dongsan.domains.walkway.service;

import com.dongsan.domains.walkway.entity.QLikedWalkway;
import com.dongsan.domains.walkway.entity.QWalkway;
import com.dongsan.domains.walkway.repository.WalkwayQueryDSLRepository;
import com.querydsl.core.Tuple;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalkwayQueryService {

    private final WalkwayQueryDSLRepository walkwayQueryDSLRepository;

    public Map<String, Object> getWalkwayWithLiked(Long walkwayId, Long memberId) {
        Tuple walkwayWithLiked = walkwayQueryDSLRepository.getWalkwayWithLiked(walkwayId, memberId);
        return Map.of(
                "walkway", walkwayWithLiked.get(QWalkway.walkway),
                "likedWalkway", walkwayWithLiked.get(QLikedWalkway.likedWalkway)
        );
    }
}
