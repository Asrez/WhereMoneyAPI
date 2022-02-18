package com.asrez.wheremoney.api.exception;

public class ApiRequestException extends RuntimeException {
    private final String code;

    public ApiRequestException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

