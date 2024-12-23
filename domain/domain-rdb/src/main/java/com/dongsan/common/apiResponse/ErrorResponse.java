package com.dongsan.common.apiResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"isSuccess", "code", "message"})
public class ErrorResponse {

    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    @Builder
    private ErrorResponse(String code, String message, List<ValidationError> errors){
        this.isSuccess = false;
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

}