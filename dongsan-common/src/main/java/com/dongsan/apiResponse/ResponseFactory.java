package com.dongsan.apiResponse;

import com.dongsan.error.code.BaseErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseFactory {
    public static final String OK_MESSAGE = "요청이 성공적으로 처리되었습니다.";
    public static final String CREATED_MESSAGE = "리소스가 성공적으로 생성되었습니다.";

    private ResponseFactory(){
        throw new UnsupportedOperationException("ResponseFactory는 인스턴스를 생성할 수 없습니다.");
    }

    public static <T> ResponseEntity<SuccessResponse<T>> ok(T result){
        return onSuccess(HttpStatus.OK, OK_MESSAGE, result);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> created(T result){
        return onSuccess(HttpStatus.CREATED, CREATED_MESSAGE ,result);
    }

    public static ResponseEntity<Void> noContent(){
        return ResponseEntity.noContent().build();
    }

    private static <T> ResponseEntity<SuccessResponse<T>> onSuccess(HttpStatus httpStatus, String message, T result){
        SuccessResponse<T> body = SuccessResponse.<T>builder()
                .message(message)
                .result(result)
                .build();

        return ResponseEntity.status(httpStatus).body(body);
    }

    public static ResponseEntity<ErrorResponse> onFailure(BaseErrorCode errorCode){
        ErrorResponse body = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }

    public static ResponseEntity<ErrorResponse> onFailure(BaseErrorCode errorCode, String message){
        ErrorResponse body = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(message)
                .build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }

    public static ResponseEntity<Object> onFailure(BaseErrorCode errorCode, List<ValidationError> errors){
        ErrorResponse body = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(errors)
                .build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }



}
