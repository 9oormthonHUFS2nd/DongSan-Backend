package com.dongsan.apiPayload.exception.handler;


import com.dongsan.apiPayload.code.BaseErrorCode;
import com.dongsan.apiPayload.exception.GeneralException;

public class CustomExceptionHandler extends GeneralException {
    public CustomExceptionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
