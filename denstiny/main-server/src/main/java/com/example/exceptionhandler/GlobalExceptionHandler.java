package com.example.exceptionhandler;

import api.Api;
import error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE) // 클수록 나중에 처리되는 Exception Handler
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
     public ResponseEntity<Api<Object>> exception(Exception exception){

        log.error("",exception); // stack trace 를 어디서, 잘 찍는 것이  중요하다!

        return ResponseEntity
                .status(500)
                .body(Api.ERROR(ErrorCode.SERVER_ERROR));
    }
}
