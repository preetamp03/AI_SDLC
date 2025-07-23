package com.example.exception;

import com.example.dto.ErrorDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation exceptions from @Valid annotation.
     * @param ex The MethodArgumentNotValidException instance.
     * @return A ResponseEntity with a 400 Bad Request status and error details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation error: {}", errors);
        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.value(), "Validation failed: " + errors, "Bad Request");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ResourceNotFoundException.
     * @param ex The ResourceNotFoundException instance.
     * @return A ResponseEntity with a 404 Not Found status.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND.value(), ex.getMessage(), "Not Found");
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles UnauthorizedException.
     * @param ex The UnauthorizedException instance.
     * @return A ResponseEntity with a 401 Unauthorized status.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDto> handleUnauthorizedException(UnauthorizedException ex) {
        log.warn("Unauthorized access attempt: {}", ex.getMessage());
        ErrorDto errorDto = new ErrorDto(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), "Unauthorized");
        return new ResponseEntity<>(errorDto, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles ForbiddenException.
     * @param ex The ForbiddenException instance.
     * @return A ResponseEntity with a 403 Forbidden status.
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorDto> handleForbiddenException(ForbiddenException ex) {
        log.warn("Forbidden access attempt: {}", ex.getMessage());
        ErrorDto errorDto = new ErrorDto(HttpStatus.FORBIDDEN.value(), ex.getMessage(), "Forbidden");
        return new ResponseEntity<>(errorDto, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles BadRequestException.
     * @param ex The BadRequestException instance.
     * @return A ResponseEntity with a 400 Bad Request status.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequestException(BadRequestException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), "Bad Request");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other uncaught exceptions as a fallback.
     * @param ex The Exception instance.
     * @return A ResponseEntity with a 500 Internal Server Error status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGlobalException(Exception ex) {
        log.error("An unexpected error occurred", ex);
        ErrorDto errorDto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An internal server error occurred.", "Internal Server Error");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
```java
// src/main/java/com/example/exception/ResourceNotFoundException.java