package com.errorfreetext.exception;

import java.time.Instant;

public class ApiErrorResponse {
    private String errorMessage;
    private int errorCode;
    private Instant timestamp;
    private String path;

    public ApiErrorResponse(String errorMessage, int errorCode, Instant timestamp, String path) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
        this.path = path;
    }

    public String errorMessage() {
        return errorMessage;
    }

    public int errorCode() {
        return errorCode;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public String path() {
        return path;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
