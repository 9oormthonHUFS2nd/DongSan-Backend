package com.dongsan.core.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoreException extends RuntimeException {
    private CoreErrorCode errorCode;

}