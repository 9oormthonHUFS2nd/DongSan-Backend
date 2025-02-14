package com.dongsan.core.domains.walkway.enums;

import com.dongsan.core.common.error.CoreErrorCode;
import com.dongsan.core.common.error.CoreException;
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
                .orElseThrow(() -> new CoreException(CoreErrorCode.INVALID_SEARCH_TYPE));
    }
}
