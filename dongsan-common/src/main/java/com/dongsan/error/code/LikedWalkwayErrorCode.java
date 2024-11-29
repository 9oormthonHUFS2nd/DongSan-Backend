package com.dongsan.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LikedWalkwayErrorCode implements BaseErrorCode{
    LIKED_WALKWAY_NOT_FOUND(HttpStatus.NOT_FOUND, "LIKED_WALKWAY-001", "좋아하는 산책로로 저장하지 않은 산책로 입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
