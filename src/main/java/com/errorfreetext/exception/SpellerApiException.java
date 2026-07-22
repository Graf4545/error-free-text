package com.errorfreetext.exception;

public class SpellerApiException extends RuntimeException {

    public SpellerApiException(String message) {
        super(message);
    }

    public SpellerApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
