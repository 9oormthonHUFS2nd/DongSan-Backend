package com.dongsan.domains.walkway.service;

import com.dongsan.domains.walkway.repository.WalkwayQueryDSLRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalkwayQueryService {

    private final WalkwayQueryDSLRepository walkwayQueryDSLRepository;

    public Tuple getWalkwayWithLiked(Long walkwayId, Long memberId) {
        return walkwayQueryDSLRepository.getWalkwayWithLiked(walkwayId, memberId);
    }
}
