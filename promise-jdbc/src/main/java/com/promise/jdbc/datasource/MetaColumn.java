package com.promise.jdbc.datasource;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by leiwei on 2019-6-18.
 */
@Data
public class MetaColumn {

    private String columnName;

    private String columnType;

    private String remarks;

    private List<String> sampleData = new ArrayList<>(200);


}
