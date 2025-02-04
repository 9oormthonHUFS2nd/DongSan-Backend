package com.dongsan.core.domains.walkway.service;

import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HashtagCommandService {

    private final HashtagRepository hashtagRepository;

    public Hashtag createHashtag(Hashtag hashtag) {
        return hashtagRepository.save(hashtag);
    }
}
