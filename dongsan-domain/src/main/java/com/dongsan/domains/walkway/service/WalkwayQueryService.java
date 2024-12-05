package com.dongsan.domains.walkway.service;

import com.dongsan.domains.walkway.dto.SearchWalkwayPopular;
import com.dongsan.domains.walkway.dto.SearchWalkwayRating;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.repository.WalkwayQueryDSLRepository;
import com.dongsan.domains.walkway.repository.WalkwayRepository;
import com.dongsan.error.code.WalkwayErrorCode;
import com.dongsan.error.exception.CustomException;
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

    public Walkway getWalkwayWithHashtagAndLike(Long userId, Long walkwayId) {
        return walkwayQueryDSLRepository.getUserWalkwayWithHashtagAndLike(userId, walkwayId);
    }

    public Walkway getWalkway(Long walkwayId) {
        return walkwayRepository.findById(walkwayId)
                .orElseThrow(() -> new CustomException(WalkwayErrorCode.WALKWAY_NOT_FOUND));
    }

    public List<Walkway> getWalkwaysPopular (
            SearchWalkwayPopular searchWalkwayPopular
    ) {
        return walkwayQueryDSLRepository.getWalkwaysPopular(searchWalkwayPopular);
    }

    public List<Walkway> getWalkwaysRating (
            SearchWalkwayRating searchWalkwayRating
    ) {
        return walkwayQueryDSLRepository.getWalkwaysRating(searchWalkwayRating);
    }

    public List<Walkway> getUserWalkWay(Long memberId, Integer size, Long walkwayId){
        return walkwayQueryDSLRepository.getUserWalkway(memberId, size, walkwayId);
    }

    public boolean existsByWalkwayId(Long walkwayId) {
        return walkwayRepository.existsById(walkwayId);
    }

    public Walkway getWalkwayWithHashtag(Long walkwayId) {
        return walkwayQueryDSLRepository.getWalkwayWithHashtag(walkwayId);
    }
}
