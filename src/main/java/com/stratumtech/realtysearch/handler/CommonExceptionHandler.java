package com.stratumtech.realtysearch.handler;

import java.util.Map;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.stratumtech.realtysearch.exception.PropertyNotFoundException;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> propertyNotFoundException(PropertyNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "error", "Property not found",
                "message", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> internalServerErrorException(RuntimeException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "error", "Internal server error",
                "message", e.getMessage()));
    }
}
