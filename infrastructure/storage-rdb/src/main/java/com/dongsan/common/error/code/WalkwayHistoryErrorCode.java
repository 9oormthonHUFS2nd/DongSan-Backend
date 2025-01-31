package com.dongsan.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WalkwayHistoryErrorCode implements BaseErrorCode {
    HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "HISTORY-001", "산책로를 이용한 기록이 없습니다."),
    NOT_ENOUGH_DISTANCE(HttpStatus.NOT_FOUND, "HISTORY-002", "충분히 산책하지 않았습니다."),
    ALREADY_REVIEWED(HttpStatus.NOT_FOUND, "HISTORY-003", "이미 리뷰를 작성하였습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
