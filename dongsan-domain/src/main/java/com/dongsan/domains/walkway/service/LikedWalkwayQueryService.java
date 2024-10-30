package com.dongsan.domains.walkway.service;

import com.dongsan.domains.walkway.repository.LikedWalkwayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikedWalkwayQueryService {
    private final LikedWalkwayRepository likedWalkwayRepository;

    public Boolean existByWalkwayIdAndMemberId(Long walkwayId, Long memberId) {
        return likedWalkwayRepository.existsByWalkway_IdAndMember_Id(walkwayId, memberId);
    }
}
