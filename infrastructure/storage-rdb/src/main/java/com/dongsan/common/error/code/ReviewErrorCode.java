package com.dongsan.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode{
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW-001", "해당 리뷰는 존재하지 않습니다."),
    INVALID_SORT_TYPE(HttpStatus.NOT_FOUND, "REVIEW-002", "유효하지 않은 정렬 타입 입니다."),
    NOT_REVIEW_OWNER(HttpStatus.CONFLICT, "REVIEW-003", "리뷰의 작성자가 아닙니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
