package com.dongsan.error.handler;

import com.dongsan.apiResponse.ErrorResponse;
import com.dongsan.apiResponse.ResponseFactory;
import com.dongsan.apiResponse.ValidationError;
import com.dongsan.error.code.BaseErrorCode;
import com.dongsan.error.code.SystemErrorStatus;
import com.dongsan.error.exception.CustomException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

// RestController 에서 발생한 예외 처리
@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    // exception 을 상속받는 모든 예외 처리
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("[Exception] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        BaseErrorCode errorCode = SystemErrorStatus.INTERNAL_SERVER_ERROR;
        return ResponseFactory.onFailure(errorCode);
    }

    // CustomException(사용자 정의 예외) 예외 처리
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("[SystemException] cause: {}, message: {}", NestedExceptionUtils.getMostSpecificCause(e),e.getMessage());
        BaseErrorCode errorCode = e.getErrorCode();
        return ResponseFactory.onFailure(errorCode);
    }

    //메소드가 잘못되었거나 부적절한 인수를 전달했을 때 -> 필수 파라미터가 없을 때
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e){
        log.error("[IlleagalArgumentException] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e),e.getMessage());
        BaseErrorCode errorCode = SystemErrorStatus.ILLEGAL_ARGUMENT_ERROR;
        String errorMessage = String.format("%s %s", errorCode.getMessage(), NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        return ResponseFactory.onFailure(errorCode, errorMessage);
    }

    // 컨트롤러 인자(@PathVariable, @RequestParam) 예외 처리
    @ExceptionHandler
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("[ConstraintViolationException] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e),e.getMessage());
        BaseErrorCode errorCode = SystemErrorStatus.INVALID_ARGUMENT_ERROR;
        List<ValidationError> errors = e.getConstraintViolations()
                .stream()
                .map(violation -> new ValidationError(violation.getPropertyPath()
                        .toString(), violation.getMessage()))
                .toList();
        return ResponseFactory.onFailure(errorCode, errors);
    }

    // DTO 유효성(@RequestBody) 관련 예외 처리
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("[MethodArgumentNotValidException] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());

        BaseErrorCode errorCode = SystemErrorStatus.INVALID_ARGUMENT_ERROR;

        List<FieldError> fieldErrors = e.getBindingResult()
                .getFieldErrors();
        List<ValidationError> errors = fieldErrors.stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        return ResponseFactory.onFailure(errorCode, errors);
    }

}
