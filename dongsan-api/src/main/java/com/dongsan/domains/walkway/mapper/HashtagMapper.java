package com.dongsan.domains.walkway.mapper;

import com.dongsan.domains.hashtag.entity.Hashtag;

public class HashtagMapper {
    public static Hashtag toHashtag(String name) {
        return Hashtag.builder()
                .name(name)
                .build();
    }
}
