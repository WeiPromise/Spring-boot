package com.promise.jdbc.vendor;

/**
 * Created by leiwei on 2019-6-18.
 */
public interface DMLProvider {

    /**
     * 根据owner(database)取表
     */
    String listTables(String Owner);

    String listDBs(String Owner);

    String sample();

    String dropTable(String tableName);
}
