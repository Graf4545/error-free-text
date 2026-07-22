package com.errorfreetext.exception;

public enum ErrorCode {
    VALIDATION_ERROR(40001),
    TASK_NOT_FOUND(40401),
    INTERNAL_ERROR(50001),
    SPELLER_ERROR(50201);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
