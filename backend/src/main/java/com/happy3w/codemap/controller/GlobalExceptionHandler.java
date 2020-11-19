package com.happy3w.codemap.controller;

import com.happy3w.toolkits.message.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<MessageResponse> exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity(
                MessageResponse.fromError(e.getMessage()),
                HttpStatus.OK);
    }
}
