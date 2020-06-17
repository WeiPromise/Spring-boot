package com.promise.jdbc.util;

/**
 * Created by leiwei on 2019-6-18.
 */
public class DsmJdbcException extends RuntimeException {
    public DsmJdbcException(String message) {
        super(message);
    }

    public DsmJdbcException(String message, Throwable cause) {
        super(message, cause);
    }
}
