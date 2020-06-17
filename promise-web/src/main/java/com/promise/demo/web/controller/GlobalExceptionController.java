package com.promise.demo.web.controller;


import com.promise.demo.util.DemoException;
import com.promise.demo.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * Created by leiwei on 2019-03-27
 * todo 全局异常处理
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler({IOException.class, DemoException.class})
    public Response handlePMSException(DemoException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Response.of(HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Response handlePMSException(IllegalArgumentException e) {
        e.printStackTrace();
        return Response.of(HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value(), "参数不合法: " + e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Response.of(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "内部服务错误:" + e.getMessage());
    }

}
