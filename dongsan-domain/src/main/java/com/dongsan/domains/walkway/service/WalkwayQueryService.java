package com.dongsan.domains.walkway.service;

import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.repository.WalkwayQueryDSLRepository;
import com.dongsan.domains.walkway.repository.WalkwayRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalkwayQueryService {

    private final WalkwayRepository walkwayRepository;
    private final WalkwayQueryDSLRepository walkwayQueryDSLRepository;

    public Walkway getWalkway(Long userId, Long walkwayId) {
        return walkwayQueryDSLRepository.getWalkway(userId, walkwayId);
    }

    public List<Walkway> getWalkwaysPopular (
            Long userId,
            Double latitude,
            Double longitude,
            int distance,
            List<String> hashtags,
            Long lastId,
            Integer lastLikes,
            int size
    ) {
        return walkwayQueryDSLRepository.getWalkwaysPopular(
                userId,
                latitude,
                longitude,
                distance,
                hashtags,
                lastId,
                lastLikes,
                size
        );
    }

    public List<Walkway> getWalkwaysRating (
            Long userId,
            Double latitude,
            Double longitude,
            int distance,
            List<String> hashtags,
            Long lastId,
            Double lastRatings,
            int size
    ) {
        return walkwayQueryDSLRepository.getWalkwaysRating(
                userId,
                latitude,
                longitude,
                distance,
                hashtags,
                lastId,
                lastRatings,
                size
        );
    }

    public List<Walkway> getUserWalkWay(Long memberId, Integer size, Long walkwayId){
        return walkwayQueryDSLRepository.getUserWalkway(memberId, size, walkwayId);
    }

    public boolean existsByWalkwayId(Long value) {
        return walkwayRepository.existsById(value);
    }

}
