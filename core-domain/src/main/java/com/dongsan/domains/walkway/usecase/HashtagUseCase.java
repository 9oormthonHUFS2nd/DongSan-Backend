package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.mapper.HashtagMapper;
import com.dongsan.domains.walkway.mapper.HashtagWalkwayMapper;
import com.dongsan.domains.walkway.service.HashtagCommandService;
import com.dongsan.domains.walkway.service.HashtagQueryService;
import com.dongsan.domains.walkway.service.HashtagWalkwayCommandService;
import java.util.ArrayList;
import java.util.List;
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

        for (String hashtagName : hashtagNames) {
            Hashtag hashtag = hashtagQueryService.findByNameOptional(hashtagName)
                    .orElseGet(() -> hashtagCommandService.createHashtag(HashtagMapper.toHashtag(hashtagName)));

            HashtagWalkway hashtagWalkway = HashtagWalkwayMapper.toHashtagWalkway(hashtag, walkway);
            hashtagWalkways.add(hashtagWalkway);
            walkway.addHashtagWalkway(hashtagWalkway);
        }

        return hashtagWalkwayCommandService.createHashtagWalkways(hashtagWalkways);
    }
}
