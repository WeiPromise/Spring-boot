package com.promise.demo.util;

import lombok.Data;

/**
 * Created by leiwei on 2019/2/13 17:07
 */
@Data
public class Request<T> {

    private T data;
}
