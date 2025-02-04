package com.dongsan.core.domains.walkway.enums;

import com.dongsan.common.error.code.WalkwayErrorCode;
import com.dongsan.common.error.exception.CustomException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WalkwaySort {
    LIKED("liked"),
    RATING("rating")
    ;

    private final String type;

    public static WalkwaySort typeOf(String type) {
        return Arrays.stream(WalkwaySort.values())
                .filter(sort -> sort.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new CustomException(WalkwayErrorCode.INVALID_SEARCH_TYPE));
    }
}
