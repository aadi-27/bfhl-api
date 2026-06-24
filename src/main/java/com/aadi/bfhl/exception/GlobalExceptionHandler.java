package com.aadi.bfhl.exception;

import com.aadi.bfhl.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global exception handler for graceful error responses.
 * Handles validation errors, malformed JSON, and unexpected exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles @Valid validation failures (e.g. null data field).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .isSuccess(false)
                .error("VALIDATION_ERROR")
                .message(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles malformed JSON or unreadable request bodies.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleMalformedJson(
            HttpMessageNotReadableException ex) {

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .isSuccess(false)
                .error("INVALID_REQUEST")
                .message("Request body is missing or malformed. Expected JSON with a 'data' array.")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Catches any unexpected runtime exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .isSuccess(false)
                .error("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred: " + ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
