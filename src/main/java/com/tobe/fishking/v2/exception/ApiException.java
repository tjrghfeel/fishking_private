package com.tobe.fishking.v2.exception;

import com.tobe.fishking.v2.enums.ErrorCodes;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException
{
    private final int errorCode;

    public ApiException(ErrorCodes errorCode)
    {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getErrorCode();
    }

    public ApiException(ErrorCodes errorCode, String message)
    {
        super(message);
        this.errorCode = errorCode.getErrorCode();
    }
}