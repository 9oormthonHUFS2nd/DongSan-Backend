package com.dongsan.domains.walkway.enums;

import com.dongsan.common.error.code.ReviewErrorCode;
import com.dongsan.common.error.exception.CustomException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReviewSort {
    LATEST("latest"),
    RATING("rating")
    ;

    private final String type;

    public static ReviewSort typeOf(String type) {
        return Arrays.stream(ReviewSort.values())
                .filter(sort -> sort.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new CustomException(ReviewErrorCode.INVALID_SORT_TYPE));
    }
}
