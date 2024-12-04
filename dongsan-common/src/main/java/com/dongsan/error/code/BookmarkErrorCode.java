package com.dongsan.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BookmarkErrorCode implements BaseErrorCode{
    BOOKMARK_NOT_EXIST(HttpStatus.NOT_FOUND, "BOOKMARK-001", "해당 북마크가 존재하지 않습니다."),
    SAME_BOOKMARK_NAME_EXIST(HttpStatus.CONFLICT, "BOOKMARK-002", "이름이 같은 북마크가 이미 존재합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
