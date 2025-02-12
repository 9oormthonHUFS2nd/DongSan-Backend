package com.dongsan.domains.walkway.service;

import com.dongsan.domains.walkway.entity.WalkwayHistory;
import com.dongsan.domains.walkway.repository.WalkwayHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalkwayHistoryCommandService {
    private final WalkwayHistoryRepository walkwayHistoryRepository;

    public WalkwayHistory createWalkwayHistory(WalkwayHistory walkwayHistory) {
        return walkwayHistoryRepository.save(walkwayHistory);
    }
}
