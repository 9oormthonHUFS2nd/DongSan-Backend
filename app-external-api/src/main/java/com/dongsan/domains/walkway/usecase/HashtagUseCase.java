package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import com.dongsan.domains.hashtag.service.HashtagCommandService;
import com.dongsan.domains.hashtag.service.HashtagQueryService;
import com.dongsan.domains.hashtag.service.HashtagWalkwayCommandService;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.mapper.HashtagMapper;
import com.dongsan.domains.walkway.mapper.HashtagWalkwayMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class HashtagUseCase {

    private final HashtagWalkwayCommandService hashtagWalkwayCommandService;

    private final HashtagQueryService hashtagQueryService;

    private final HashtagCommandService hashtagCommandService;

    @Transactional
    public List<HashtagWalkway> createHashtagWalkways(Walkway walkway, List<String> hashtagNames) {

        List<HashtagWalkway> hashtagWalkways = new ArrayList<>();
        List<Hashtag> hashtags = hashtagQueryService.getHashtagsByName(hashtagNames);

        for (String hashtagName : hashtagNames) {

            Optional<Hashtag> hashtagCheck = hashtags.stream()
                    .filter(hashtag -> hashtag.getName().equals(hashtagName))
                    .findFirst();

            HashtagWalkway hashtagWalkway;

            // 해쉬태그가 존재하지 않으면 생성 후 추가
            if (hashtagCheck.isPresent()) {
                hashtagWalkway = HashtagWalkwayMapper.toHashtagWalkway(hashtagCheck.get(), walkway);
            } else {
                Hashtag hashtag = HashtagMapper.toHashtag(hashtagName);
                hashtagCommandService.createHashtag(hashtag);
                hashtagWalkway = HashtagWalkwayMapper.toHashtagWalkway(hashtag, walkway);
            }
            hashtagWalkways.add(hashtagWalkway);
            walkway.addHashtagWalkway(hashtagWalkway);
        }

        return hashtagWalkwayCommandService.createHashtagWalkways(hashtagWalkways);
    }
}
