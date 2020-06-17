package com.promise.demo.util;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response<T> {
    private T data;
    private Integer statusCode;
    private String message;

    protected Response(){}

    private Response(T data, Integer statusCode, String message) {
        this.data = data;
        this.statusCode = statusCode;
        this.message = message;
    }


    public static <T> Response of(T data) {
        return new Response(data, HttpStatus.OK.value(), "success");
    }

    public static <T> Response of(T data, String message) {
        return new Response(data, HttpStatus.OK.value(), message);
    }

    public static <T> Response of(T data, HttpStatus httpStatus, String message) {
        return new Response(data, httpStatus.value(), message);
    }

    public static <T> Response of(T data, Integer httpStatus, String message) {
        return new Response(data, httpStatus, message);
    }
}
