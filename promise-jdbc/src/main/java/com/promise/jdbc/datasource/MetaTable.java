package com.promise.jdbc.datasource;

import lombok.Data;

import java.util.List;

/**
 * Created by leiwei on 2019-6-18.
 */
@Data
public class MetaTable {

    private String tableName;

    private String tableType;

    private String tableRemarks;

    private List<MetaColumn> columns;
}
