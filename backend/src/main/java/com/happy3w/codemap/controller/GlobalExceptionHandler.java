package com.happy3w.codemap.controller;

import com.happy3w.toolkits.message.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<MessageResponse> exceptionHandler(Exception e) {
        return new ResponseEntity(
                MessageResponse.fromError(e.getMessage()),
                HttpStatus.OK);
    }
}
