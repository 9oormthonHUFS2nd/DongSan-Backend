package com.dongsan.core.domains.walkway.service;


import com.dongsan.core.domains.walkway.CreateWalkway;
import com.dongsan.core.domains.walkway.UpdateWalkway;
import com.dongsan.core.domains.walkway.WalkwayRepository;
import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
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
        return walkwayRepository.saveWalkway(createWalkway)
                .orElseThrow(() -> new CoreException(CoreErrorCode.CANT_CREATE_WALKWAY));
    }

    public void updateWalkway(UpdateWalkway updateWalkway) {
        walkwayRepository.updateWalkway(updateWalkway);
    }

    public void saveLikedWalkway(Long memberId, Long walkwayId) {
        walkwayRepository.saveLikedWalkway(memberId, walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.CANT_CREATE_LIKED_WALKWAY));
    }

    public void deleteLikedWalkway(Long memberId, Long walkwayId) {
        walkwayRepository.deleteLikedWalkway(memberId, walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.CANT_DELETE_LIKED_WALKWAY));
    }
}
