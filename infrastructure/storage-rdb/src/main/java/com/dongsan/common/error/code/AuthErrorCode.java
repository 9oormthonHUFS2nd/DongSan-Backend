package com.dongsan.common.error.code;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode{
    // 인증 및 권한 관련 에러
    AUTHENTICATION_FAILED(UNAUTHORIZED, "AUTH-01", "사용자 인증에 실패했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH-02", "접근이 거부되었습니다. 이 리소스에 대한 권한이 없습니다."),
    INSUFFICIENT_PERMISSIONS(HttpStatus.FORBIDDEN, "AUTH-03", "작업을 수행할 권한이 부족합니다."),
    LOGIN_FAILED(UNAUTHORIZED, "AUTH-04", "로그인에 실패했습니다."),

    // 토큰 관련 에러
    ACCESS_TOKEN_EXPIRED(UNAUTHORIZED, "AUTH-05", "엑세스 토큰의 유효기간이 만료되었습니다."),
    REFRESH_TOKEN_EXPIRED(UNAUTHORIZED, "AUTH-06", "리프레시 토큰의 유효기간이 만료되었습니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "AUTH-07", "잘못된 토큰 형식입니다."),
    INVALID_TOKEN_SIGNATURE(HttpStatus.BAD_REQUEST, "AUTH-08", "토큰의 서명이 일치하지 않습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "AUTH-09", "토큰의 특정 헤더나 클레임이 지원되지 않습니다."),
    ACCESS_TOKEN_NOT_FOUND(UNAUTHORIZED, "AUTH-10", "쿠키에 엑세스 토큰이 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED, "AUTH-11", "쿠키에 리프레시 토큰이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
