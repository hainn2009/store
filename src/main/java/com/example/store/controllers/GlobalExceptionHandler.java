package com.example.store.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.store.dtos.ErrorDto;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleUnreadableMessage() {
        return ResponseEntity.badRequest().body(new ErrorDto("Invalid request body"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException exception
    ) {
        var errors = new HashMap<String, String>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() == UUID.class) {
            return ResponseEntity
                .badRequest()
                .body(Map.of(
                    "error", "Invalid UUID format",
                    "field", ex.getName(),
                    "message", ex.getName() + " không hợp lệ. Vui lòng truyền đúng định dạng UUID."
                ));
        }

        return ResponseEntity
            .badRequest()
            .body(Map.of(
                "error", "Invalid parameter",
                "field", ex.getName(),
                "message", ex.getMessage()
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllOtherExceptions(Exception ex) {
        System.err.println("Unhandled exception: " + ex.getMessage());
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Internal Server Error: " + ex.getMessage());
    }
}
