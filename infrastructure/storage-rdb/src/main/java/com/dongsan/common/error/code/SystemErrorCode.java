package com.dongsan.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SystemErrorCode implements BaseErrorCode {

    // 일반적인 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYSTEM-001", "서버 내부 오류가 발생했습니다. 다시 시도해주세요."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "SYS-002", "현재 서비스 이용이 불가능합니다. 나중에 다시 시도해주세요."),
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "SYS-003", "요청 시간이 초과되었습니다. 다시 시도해주세요."),

    ILLEGAL_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "CLIENT-001", "잘못된 요청입니다. 요청 내용을 다시 확인해주세요."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "CLIENT-002", "요청한 리소스를 찾을 수 없습니다."),
    INVALID_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "CLIENT-003", "올바르지 않은 요청입니다. 요청 내용을 다시 확인해주세요."),
    INVALID_FORMAT_ERROR(HttpStatus.BAD_REQUEST,"CLIENT-004", "올바르지 않은 포맷입니다."),
    INVALID_TYPE_ERROR(HttpStatus.BAD_REQUEST, "CLIENT-005", "올바르지 않은 타입입니다."),
    INVALID_HTTP_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "CLIENT-006", "잘못된 Http Method 요청입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
