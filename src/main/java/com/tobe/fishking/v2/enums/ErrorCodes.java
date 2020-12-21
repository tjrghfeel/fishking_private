package com.tobe.fishking.v2.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ErrorCodes {

    PARAMETER_ERROR(10000, "파라미터에 오류가 있습니다."),
    VALIDATION_ERROR(10001, "Validation 오류가 있습니다."),

    INTERNAL_SERVER_ERROR(20000, "서버 오류가 발생하였습니다."),
    DB_INSERT_ERROR(20001, "DB Insert 오류가 발생하였습니다."),
    DB_ERROR(20002, "데이터 베이스 오류가 발생했습니다."),
    NOT_FOUND_ERROR(20004, "찾는 값이 없습니다."),
    DB_DUPLICATION_ERROR(20005, "중복된 값이 존재합니다.");

    private final int errorCode;
    private final String message;

    ErrorCodes(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ErrorCodes findByCode(int errorCode) {
        return Arrays.stream(ErrorCodes.values())
                .filter(errorCodes -> (errorCodes.getErrorCode() == errorCode))
                .findAny()
                .orElse(INTERNAL_SERVER_ERROR);
    }

}
