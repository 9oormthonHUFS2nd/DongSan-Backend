package com.dongsan.core.domains.walkway.service;


import com.dongsan.core.domains.walkway.CreateWalkway;
import com.dongsan.core.domains.walkway.UpdateWalkway;
import com.dongsan.core.domains.walkway.WalkwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WalkwayWriter {

    @Autowired


    public WalkwayWriter(WalkwayRepository walkwayRepository) {
        this.walkwayRepository = walkwayRepository;
    }

    private final WalkwayRepository walkwayRepository;

    public Long saveWalkway(CreateWalkway createWalkway) {
        return walkwayRepository.saveWalkway(createWalkway);
    }

    public void updateWalkway(UpdateWalkway updateWalkway) {
        walkwayRepository.updateWalkway(updateWalkway);
    }

    public Long saveLikedWalkway(Long memberId, Long walkwayId) {
        return walkwayRepository.saveLikedWalkway(memberId, walkwayId);
    }

    public void deleteLikedWalkway(Long memberId, Long walkwayId) {
        walkwayRepository.deleteLikedWalkway(memberId, walkwayId);
    }
}
