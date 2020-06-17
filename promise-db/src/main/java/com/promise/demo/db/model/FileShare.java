package com.promise.demo.db.model;

import lombok.Data;

/**
 * Created by leiwei on 2020/6/1 15:26
 */
@Data
public class FileShare {

    private Integer id;

    private byte[] file;

    private String fileName;

    private String createTime;

    private String expireTime;
}
