package com.happy3w.codemap.controller;

import com.happy3w.toolkits.message.MessageResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public MessageResponse exceptionHandler(Exception e) {
        System.out.println("未知异常！原因是:" + e);
        return MessageResponse.fromError(e.getMessage());
    }
}
