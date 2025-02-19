package com.dongsan.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WalkwayErrorCode implements BaseErrorCode {
    INVALID_COURSE(HttpStatus.NOT_FOUND, "WALKWAY-001", "유효하지 않은 산책 경로입니다."),
    INVALID_SEARCH_TYPE(HttpStatus.NOT_FOUND, "WALKWAY-002", "유효하지 산책로 검색 타입 입니다."),
    WALKWAY_NOT_FOUND(HttpStatus.NOT_FOUND, "WALKWAY-003", "존재하지 않는 산책로 입니다."),
    NOT_WALKWAY_OWNER(HttpStatus.NOT_FOUND, "WALKWAY-004", "권한이 없는 산책로 입니다."),
    INVALID_COURSE_IMAGE(HttpStatus.NOT_FOUND, "WALKWAY-005", "유효하지 않은 산책 경로 이미지입니다."),
    WALKWAY_PRIVATE(HttpStatus.NOT_FOUND, "WALKWAY-006", "비공개 산책로 입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
