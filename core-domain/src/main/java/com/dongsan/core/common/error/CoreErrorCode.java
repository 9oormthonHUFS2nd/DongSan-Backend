package com.dongsan.core.common.error;

public enum CoreErrorCode {
    // system error
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



    // 인증 및 권한 관련 에러
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH-001", "사용자 인증에 실패했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH-002", "접근이 거부되었습니다. 이 리소스에 대한 권한이 없습니다."),
    INSUFFICIENT_PERMISSIONS(HttpStatus.FORBIDDEN, "AUTH-003", "작업을 수행할 권한이 부족합니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH-004", "로그인에 실패했습니다."),

    // 토큰 관련 에러
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-005", "엑세스 토큰의 유효기간이 만료되었습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-005", "리프레시 토큰의 유효기간이 만료되었습니다."),
    TERMS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-006", "약관 동의 토큰의 유효기간이 만료되었습니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "AUTH-007", "잘못된 토큰 형식입니다."),
    INVALID_TOKEN_SIGNATURE(HttpStatus.BAD_REQUEST, "AUTH-008", "토큰의 서명이 일치하지 않습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "AUTH-009", "토큰의 특정 헤더나 클레임이 지원되지 않습니다."),

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH-010", "쿠키에 리프레시 토큰이 없습니다."),
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH-011", "요청 헤더에 엑세스 토큰이 없습니다."),


    // Bookmark
    BOOKMARK_NOT_EXIST(HttpStatus.NOT_FOUND, "BOOKMARK-001", "해당 북마크가 존재하지 않습니다."),
    SAME_BOOKMARK_NAME_EXIST(HttpStatus.CONFLICT, "BOOKMARK-002", "이름이 같은 북마크가 이미 존재합니다."),
    NOT_BOOKMARK_OWNER(HttpStatus.CONFLICT, "BOOKMARK-003", "해당 북마크의 생성자가 아닙니다."),
    WALKWAY_ALREADY_EXIST_IN_BOOKMARK(HttpStatus.CONFLICT, "BOOKMARK-005", "북마크에 이미 존재하는 산책로입니다."),
    WALKWAY_NOT_EXIST_IN_BOOKMARK(HttpStatus.CONFLICT, "BOOKMARK-006", "북마크에 존재하지 않는 산책로입니다."),


    // image
    IMAGE_NOT_EXISTS(HttpStatus.NOT_FOUND, "IMAGE-001", "존재하지 않은 이미지입니다."),

    // Walkway
    INVALID_COURSE(HttpStatus.NOT_FOUND, "WALKWAY-001", "유효하지 않은 산책 경로입니다."),
    INVALID_SEARCH_TYPE(HttpStatus.NOT_FOUND, "WALKWAY-002", "유효하지 산책로 검색 타입 입니다."),
    WALKWAY_NOT_FOUND(HttpStatus.NOT_FOUND, "WALKWAY-003", "존재하지 않는 산책로 입니다."),
    NOT_WALKWAY_OWNER(HttpStatus.NOT_FOUND, "WALKWAY-004", "권한이 없는 산책로 입니다."),
    INVALID_COURSE_IMAGE(HttpStatus.NOT_FOUND, "WALKWAY-005", "유효하지 않은 산책 경로 이미지입니다."),
    WALKWAY_PRIVATE(HttpStatus.NOT_FOUND, "WALKWAY-006", "비공개 산책로 입니다."),
    LIKED_WALKWAY_NOT_FOUND(HttpStatus.NOT_FOUND, "LIKED_WALKWAY-001", "좋아하는 산책로로 저장하지 않은 산책로 입니다."),


    // member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-001", "해당 회원이 존재하지 않습니다."),

    // review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW-001", "해당 리뷰는 존재하지 않습니다."),
    INVALID_SORT_TYPE(HttpStatus.NOT_FOUND, "REVIEW-002", "유효하지 않은 정렬 타입 입니다."),
    NOT_REVIEW_OWNER(HttpStatus.CONFLICT, "REVIEW-003", "리뷰의 작성자가 아닙니다."),
    ;



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    CoreErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    HttpStatus getHttpStatus(){
        return httpStatus;
    }

    String getCode(){
        return code;
    }

    String getMessage(){
        return message;
    }

}
