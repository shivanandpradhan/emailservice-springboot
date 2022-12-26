package com.project.emailservice.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final String successKey = "success";
    private final String messageKey = "message";
    
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleExceptionClass(Exception exception){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(successKey, false);
        errorResponse.put(messageKey, exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
