package com.dongsan.core.domains.walkway.service;


import com.dongsan.core.domains.walkway.WalkwayRepository;
import com.dongsan.core.domains.walkway.domain.LikedWalkway;
import com.dongsan.core.domains.walkway.domain.Walkway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalkwayWriter {

    private final WalkwayRepository walkwayRepository;

    public Walkway saveWalkway(Walkway walkway) {
        return walkwayRepository.saveWalkway(walkway);
    }

    public void updateWalkway(Walkway updateWalkway) {
        walkwayRepository.updateWalkway(updateWalkway);
    }

    public LikedWalkway saveLikedWalkway(LikedWalkway likedWalkway) {
        return walkwayRepository.saveLikedWalkway(likedWalkway);
    }

    public void deleteLikedWalkway(LikedWalkway likedWalkway) {
        walkwayRepository.deleteLikedWalkway(likedWalkway);
    }
}
