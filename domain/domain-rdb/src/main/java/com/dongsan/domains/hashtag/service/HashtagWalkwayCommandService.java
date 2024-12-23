package com.dongsan.domains.hashtag.service;

import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import com.dongsan.domains.hashtag.repository.HashtagWalkwayRepository;
import com.dongsan.domains.walkway.entity.Walkway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HashtagWalkwayCommandService {
    private final HashtagWalkwayRepository hashtagWalkwayRepository;

    public List<HashtagWalkway> createHashtagWalkways(List<HashtagWalkway> hashtagWalkways) {
        return hashtagWalkwayRepository.saveAll(hashtagWalkways);
    }

    public void deleteAllHashtagWalkways(Walkway walkway) {
        hashtagWalkwayRepository.deleteAllByWalkway(walkway);
    }
}
