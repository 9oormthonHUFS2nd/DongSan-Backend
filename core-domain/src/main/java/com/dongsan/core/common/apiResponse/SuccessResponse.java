package com.dongsan.core.common.apiResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"isSuccess", "message", "result"})
public class SuccessResponse<T> {

    private final Boolean isSuccess;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    @Builder
    private SuccessResponse(String message, T data){
        this.isSuccess = true;
        this.message = message;
        this.data = data;
    }

}
