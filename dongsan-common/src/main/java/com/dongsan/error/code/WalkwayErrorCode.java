package com.dongsan.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WalkwayErrorCode {
    INVALID_COURSE(HttpStatus.NOT_FOUND, "WALKWAY-001", "유효하지 않은 산책 경로입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
