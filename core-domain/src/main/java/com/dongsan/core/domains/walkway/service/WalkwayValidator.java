package com.dongsan.core.domains.walkway.service;

import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayRepository;
import com.dongsan.core.domains.walkway.enums.ExposeLevel;
import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WalkwayValidator {
    private final WalkwayRepository walkwayRepository;

    @Autowired
    public WalkwayValidator(WalkwayRepository walkwayRepository) {
        this.walkwayRepository = walkwayRepository;
    }

    public void isOwnerOfWalkway(Long walkwayId, Long memberId){
        boolean result = walkwayRepository.existsWalkway(walkwayId, memberId);
        if(!result){
            throw new CoreException(CoreErrorCode.NOT_WALKWAY_OWNER);
        }
    }

    public void validateWalkwayExists(Long walkwayId) {
        boolean result = walkwayRepository.existsWalkway(walkwayId);
        if(!result){
            throw new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND);
        }
    }

    public void validateWalkwayPrivate(Long walkwayId) {
        Walkway walkway = walkwayRepository.getWalkway(walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND));

        if (walkway.exposeLevel().equals(ExposeLevel.PRIVATE)) {
            throw new CoreException(CoreErrorCode.WALKWAY_PRIVATE);
        }
    }
}
