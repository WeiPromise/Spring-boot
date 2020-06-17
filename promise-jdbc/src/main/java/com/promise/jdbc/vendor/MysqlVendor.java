package com.promise.jdbc.vendor;

import com.mysql.cj.jdbc.Driver;

import java.sql.SQLException;

/**
 * Created by leiwei on 2019-6-18.
 */
@VendorIdentifier(name = "Mysql")
public class MysqlVendor extends Driver implements Vendor {

    public MysqlVendor() throws SQLException {}

    @Override
    public String listTables(String Owner) {
        return "select table_name from information_schema.tables where table_schema='" + Owner + "' and table_type='base table';";
    }

    @Override
    public String listDBs(String Owner) {
        return null;
    }

    @Override
    public String sample() {
        return null;
    }

    @Override
    public String dropTable(String tableName) {
        return null;
    }
}
