package com.promise.demo.db.model;

import lombok.Data;

/**
 * Created by leiwei on 2020/6/1 15:26
 */
@Data
public class FileSharePO {

    private Integer id;

    private Object file;

    private String fileName;

    private String createTime;

    private String expireTime;
}
