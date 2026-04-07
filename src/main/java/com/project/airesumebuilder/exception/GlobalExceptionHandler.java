// Package where all exception handling classes are placed
package com.project.airesumebuilder.exception;

// Provides standard HTTP status codes (400, 409, 500, etc.)
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

// Used to send HTTP response with status + body
import org.springframework.http.ResponseEntity;

// Represents a validation error on a specific field
import org.springframework.validation.FieldError;

// Exception thrown when @Valid validation fails
import org.springframework.web.bind.MethodArgumentNotValidException;

// Used to define methods that handle specific exceptions
import org.springframework.web.bind.annotation.ExceptionHandler;

// Enables global exception handling across all controllers
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap; // Implementation of Map (stores key-value pairs)
import java.util.Map; // Interface for key-value data structure
@Slf4j
@RestControllerAdvice // Applies this class globally to handle exceptions from all REST controllers
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // Handles validation errors triggered by @Valid annotation
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException exception) {
        log.info("Inside GlobalExceptionHandler: handleValidationExceptions() ", exception);
        Map<String, String> errors = new HashMap<>(); // Stores field name → error message

        // Iterates through all validation errors
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError)error).getField(); // Gets the field name where validation failed
            String errorMessage = error.getDefaultMessage(); // Gets the validation error message
            errors.put(fieldName, errorMessage); // Adds field and its error message into map
        });

        Map<String, Object> response = new HashMap<>(); // Final response structure
        response.put("message", "validation failed"); // General message for client
        response.put("errors", errors); // Detailed field-level errors
        // Returns HTTP 400 (Bad Request) with response body
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceExistsException.class) // Handles custom exception when resource already exists
    public ResponseEntity<Map<String, Object>> handleResourceExistsException(ResourceExistsException exception) {
        log.info("Inside GlobalExceptionHandler: handleResourceExistsException() ", exception);
        Map<String, Object> response = new HashMap<>(); // Response structure
        response.put("message", "Resource exists"); // General error message
        response.put("errors", exception.getMessage()); // Actual exception message (e.g., duplicate email)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // Returns HTTP 409 (Conflict)
    }

    @ExceptionHandler(Exception.class) //  Handles all other unhandled exceptions globally
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception exception) {
        log.info("Inside GlobalExceptionHandler: handleGenericExceptions() ", exception);
        Map<String, Object> response = new HashMap<>(); // Response structure
        response.put("message", "Something went wrong. Contact Administrator"); // Generic message for unexpected errors
        response.put("errors", exception.getMessage()); // Actual error message (use carefully in production)
        // Returns HTTP 500 (Internal Server Error)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}