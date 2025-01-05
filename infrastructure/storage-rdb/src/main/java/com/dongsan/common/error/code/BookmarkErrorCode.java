package com.dongsan.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BookmarkErrorCode implements BaseErrorCode{
    BOOKMARK_NOT_EXIST(HttpStatus.NOT_FOUND, "BOOKMARK-001", "해당 북마크가 존재하지 않습니다."),
    SAME_BOOKMARK_NAME_EXIST(HttpStatus.CONFLICT, "BOOKMARK-002", "이름이 같은 북마크가 이미 존재합니다."),
    NOT_BOOKMARK_OWNER(HttpStatus.CONFLICT, "BOOKMARK-003", "해당 북마크의 생성자가 아닙니다."),
    WALKWAY_ALREADY_EXIST_IN_BOOKMARK(HttpStatus.CONFLICT, "BOOKMARK-005", "북마크에 이미 존재하는 산책로입니다."),
    WALKWAY_NOT_EXIST_IN_BOOKMARK(HttpStatus.CONFLICT, "BOOKMARK-006", "북마크에 존재하지 않는 산책로입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
