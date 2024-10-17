package com.dongsan.domains.hashtag.service;

import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashtagQueryService {
    private final HashtagRepository hashtagRepository;

    public List<Hashtag> getHashtagsByName(List<String> hashtagNames) {
        return hashtagRepository.findByNameIn(hashtagNames);
    }
}
