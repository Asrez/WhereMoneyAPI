package com.asrez.wheremoney.api.exception;

import org.springframework.http.HttpStatus;

public class ApiException {
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    public ApiException(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }
}
