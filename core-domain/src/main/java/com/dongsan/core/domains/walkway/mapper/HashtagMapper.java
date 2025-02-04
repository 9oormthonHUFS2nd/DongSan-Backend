package com.dongsan.core.domains.walkway.mapper;

import com.dongsan.domains.hashtag.entity.Hashtag;

public class HashtagMapper {
    private HashtagMapper(){}
    public static Hashtag toHashtag(String name) {
        return Hashtag.builder()
                .name(name)
                .build();
    }
}
