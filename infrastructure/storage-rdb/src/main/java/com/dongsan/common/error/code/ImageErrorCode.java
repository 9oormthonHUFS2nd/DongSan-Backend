package com.dongsan.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ImageErrorCode implements BaseErrorCode {
    IMAGE_NOT_EXISTS(HttpStatus.NOT_FOUND, "IMAGE-001", "존재하지 않은 이미지입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
