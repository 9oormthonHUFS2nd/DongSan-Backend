package com.dongsan.core.domains.walkway.usecase;

import com.dongsan.core.domains.walkway.service.HashtagWriter;
import com.dongsan.core.domains.walkway.service.HashtagReader;
import com.dongsan.core.domains.walkway.service.HashtagWalkwayWriter;
import com.dongsan.core.common.annotation.UseCase;
import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.core.domains.walkway.mapper.HashtagMapper;
import com.dongsan.core.domains.walkway.mapper.HashtagWalkwayMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagWalkwayWriter hashtagWalkwayWriter;
    private final HashtagReader hashtagReader;
    private final HashtagWriter hashtagWriter;

    @Transactional
    public List<HashtagWalkway> createHashtagWalkways(Walkway walkway, List<String> hashtagNames) {
        List<HashtagWalkway> hashtagWalkways = new ArrayList<>();

        for (String hashtagName : hashtagNames) {
            Hashtag hashtag = hashtagReader.findByNameOptional(hashtagName)
                    .orElseGet(() -> hashtagWriter.createHashtag(HashtagMapper.toHashtag(hashtagName)));

            HashtagWalkway hashtagWalkway = HashtagWalkwayMapper.toHashtagWalkway(hashtag, walkway);
            hashtagWalkways.add(hashtagWalkway);
            walkway.addHashtagWalkway(hashtagWalkway);
        }

        return hashtagWalkwayWriter.createHashtagWalkways(hashtagWalkways);
    }
}
