package com.dongsan.core.domains.walkway.service;

import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.repository.WalkwayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalkwayWriter {

    private final WalkwayRepository walkwayRepository;

    public Walkway createWalkway(Walkway walkway) {
        return walkwayRepository.save(walkway);
    }
}
