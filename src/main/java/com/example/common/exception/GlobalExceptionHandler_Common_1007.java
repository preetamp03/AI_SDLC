package com.example.common.exception;

import com.example.api.order.exception.OrderValidationException_Order_4008;
import com.example.api.user.exception.EmailConflictException_User_2005;
import com.example.common.logging.StructuredLogger_API_1001;
import com.example.common.model.ErrorCode_Common_1004;
import com.example.common.model.ErrorResponse_Common_1003;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler to standardize error responses across the application.
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler_Common_1007 {

    private final StructuredLogger_API_1001 logger;

    /**
     * Handles validation errors for @Valid request bodies. (400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse_Common_1003> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value"
                ));

        ErrorResponse_Common_1003 errorResponse = ErrorResponse_Common_1003.builder()
                .errorCode(ErrorCode_Common_1004.INVALID_INPUT.name())
                .message("One or more fields are missing or invalid.")
                .invalidFields(errors)
                .build();
        logger.logError("Validation error", ex, "invalidFields", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles validation errors for path variables and request parameters. (400 Bad Request)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse_Common_1003> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
            .collect(Collectors.toMap(
                cv -> cv.getPropertyPath().toString(),
                ConstraintViolation::getMessage
            ));

        ErrorResponse_Common_1003 errorResponse = ErrorResponse_Common_1003.builder()
                .errorCode(ErrorCode_Common_1004.INVALID_INPUT.name())
                .message("One or more parameters are invalid.")
                .invalidFields(errors)
                .build();
        logger.logError("Constraint violation error", ex, "invalidFields", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles invalid UUID format in path variables. (400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse_Common_1003> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message;
        ErrorCode_Common_1004 code;

        if (ex.getRequiredType() != null && ex.getRequiredType().equals(java.util.UUID.class)) {
            message = "The product ID provided is not in a valid format.";
            code = ErrorCode_Common_1004.INVALID_PRODUCT_ID;
        } else {
            message = "Invalid parameter format.";
            code = ErrorCode_Common_1004.INVALID_INPUT;
        }

        ErrorResponse_Common_1003 errorResponse = ErrorResponse_Common_1003.builder()
            .errorCode(code.name())
            .message(message)
            .build();
        logger.logError("Parameter type mismatch error", ex, "parameter", ex.getName());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles authentication failures. (401 Unauthorized)
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse_Common_1003> handleAuthenticationException(AuthenticationException ex) {
         ErrorResponse_Common_1003 errorResponse = ErrorResponse_Common_1003.builder()
            .errorCode(ErrorCode_Common_1004.UNAUTHORIZED.name())
            .message("Authentication token is missing, expired, or invalid.")
            .build();
        logger.logError("Authentication failed", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles resource not found errors. (404 Not Found)
     */
    @ExceptionHandler(ResourceNotFoundException_Common_1005.class)
    public ResponseEntity<ErrorResponse_Common_1003> handleResourceNotFoundException(ResourceNotFoundException_Common_1005 ex) {
        ErrorResponse_Common_1003 errorResponse = ErrorResponse_Common_1003.builder()
                .errorCode(ex.getErrorCode().name())
                .message(ex.getMessage())
                .build();
        logger.logError("Resource not found", ex, "errorCode", ex.getErrorCode().name());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles email conflict errors during user creation. (409 Conflict)
     */
    @ExceptionHandler(EmailConflictException_User_2005.class)
    public ResponseEntity<ErrorResponse_Common_1003> handleEmailConflictException(EmailConflictException_User_2005 ex) {
        ErrorResponse_Common_1003 errorResponse = ErrorResponse_Common_1003.builder()
                .errorCode(ErrorCode_Common_1004.EMAIL_EXISTS.name())
                .message(ex.getMessage())
                .build();
        logger.logError("Email conflict", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handles business logic validation failures for orders. (422 Unprocessable Entity)
     */
    @ExceptionHandler(OrderValidationException_Order_4008.class)
    public ResponseEntity<ErrorResponse_Common_1003> handleOrderValidationException(OrderValidationException_Order_4008 ex) {
        ErrorResponse_Common_1003 errorResponse = ErrorResponse_Common_1003.builder()
                .errorCode(ErrorCode_Common_1004.ORDER_VALIDATION_FAILED.name())
                .message(ex.getMessage())
                .details(ex.getDetails())
                .build();
        logger.logError("Order validation failed", ex, "details", ex.getDetails());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handles all other uncaught exceptions. (500 Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse_Common_1003> handleAllOtherExceptions(Exception ex) {
        ErrorResponse_Common_1003 errorResponse = ErrorResponse_Common_1003.builder()
                .errorCode(ErrorCode_Common_1004.SERVER_ERROR.name())
                .message("An unexpected error occurred on the server.")
                .build();
        logger.logError("An unexpected server error occurred", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
```java
// src/test/java/com/example/common/logging/Log4j2StructuredLogger_API_1002Test.java