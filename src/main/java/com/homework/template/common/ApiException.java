package com.homework.template.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;


    public ApiException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public static ApiException makeNotFound(final String message) {
        return new ApiException(HttpStatus.NOT_FOUND, message);
    }

    public static ApiException makeConflict(final String message) {
        return new ApiException(HttpStatus.CONFLICT, message);
    }

}