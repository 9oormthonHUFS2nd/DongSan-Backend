package com.dongsan.domains.walkway.mapper;

import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import com.dongsan.domains.walkway.entity.Walkway;

public class HashtagWalkwayMapper {

    public static HashtagWalkway toHashtagWalkway(Hashtag hashtag, Walkway walkway) {
        return HashtagWalkway.builder()
                .hashtag(hashtag)
                .walkway(walkway)
                .build();
    }
}
