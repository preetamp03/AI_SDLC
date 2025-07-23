package com.example.exception;

import com.example.dto.ErrorDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles custom API exceptions.
     * @param ex The exception.
     * @return A response entity with error details.
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDto> handleApiException(ApiException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getStatus().value(), ex.getMessage(), ex.getStatus().getReasonPhrase());
        log.warn("API Exception: {} - {}", ex.getStatus(), ex.getMessage());
        return new ResponseEntity<>(errorDto, ex.getStatus());
    }

    /**
     * Handles validation exceptions for @Valid annotated DTOs.
     * @param ex The exception.
     * @param headers The headers.
     * @param status The status code.
     * @param request The request.
     * @return A response entity with validation error details.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        String message = "Invalid input: " + errors;
        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.value(), message, "Bad Request");
        log.warn("Validation failed: {}", message);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles generic, un-caught exceptions.
     * @param ex The exception.
     * @return A response entity for an internal server error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGlobalException(Exception ex) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again later.", "Internal Server Error");
        log.error("Unhandled Exception: ", ex);
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
```java
// src/main/java/com/example/repository/IUserRepository.java