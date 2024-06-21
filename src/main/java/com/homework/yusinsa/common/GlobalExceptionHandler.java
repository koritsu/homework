package com.homework.yusinsa.common;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

import static com.homework.yusinsa.common.Constant.DATABASE_INTEGRITY_CONSTRAINT_VIOLATION;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse<Map<String, String>> response = new ApiResponse<>(errors, "validation failed", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {


        String customMessage = "잘못된 요청입니다.";  // 기본 메시지

        if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
            customMessage = mismatchedInputException.getPath().get(0).getFieldName() + " 필드의 값이 잘못되었습니다.";
        }

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                null,
                customMessage,
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleNoHandlerFoundException(NoResourceFoundException e) {

        var errorMessage = String.format("not found uri or resource : %s %s", e.getHttpMethod(), e.getResourcePath());

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                null,
                errorMessage,
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleJdbcSQLIntegrityConstraintViolationException(DataIntegrityViolationException e) {

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                null,
                DATABASE_INTEGRITY_CONSTRAINT_VIOLATION,
                HttpStatus.CONFLICT.value()
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleAllExceptions(Exception e) {

        ApiResponse<String> response;
        HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        log.error("", e);

        if (e instanceof final ApiException apiException) {
            response = new ApiResponse<>(null, apiException.getMessage(), apiException.getHttpStatus().value());
            responseStatus = apiException.getHttpStatus();
        } else {
            response = new ApiResponse<>(null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(response, responseStatus);
    }

}
