package com.dongsan.core.domains.walkway.service;

import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.repository.HashtagDSLRepository;
import com.dongsan.domains.hashtag.repository.HashtagRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashtagReader {
    private final HashtagRepository hashtagRepository;
    private final HashtagDSLRepository hashtagDSLRepository;

    public List<Hashtag> getHashtagsByName(List<String> hashtagNames) {
        return hashtagRepository.findByNameIn(hashtagNames);
    }

    public Optional<Hashtag> findByNameOptional(String hashtagName) {
        return hashtagRepository.findByName(hashtagName);
    }

}
