package com.dongsan.domains.walkway.enums;

import com.dongsan.common.error.code.WalkwayErrorCode;
import com.dongsan.common.error.exception.CustomException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Sort {
    LIKED("liked"),
    RATING("rating")
    ;

    private final String type;

    public static Sort typeOf(String type) {
        return Arrays.stream(Sort.values())
                .filter(sort -> sort.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new CustomException(WalkwayErrorCode.INVALID_SEARCH_TYPE));
    }
}
