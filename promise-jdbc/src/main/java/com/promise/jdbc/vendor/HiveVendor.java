package com.promise.jdbc.vendor;

import com.mysql.cj.jdbc.Driver;

import java.sql.SQLException;

/**
 * Created by leiwei on 2019-6-18.
 */
@VendorIdentifier(name = "Hive")
//public class HiveVendor extends HiveDriver implements Vendor {
public class HiveVendor extends Driver implements Vendor {

    public HiveVendor() throws SQLException {}

    @Override
    public String listTables(String Owner) {
        return "SHOW TABLES";
    }

    @Override
    public String listDBs(String Owner) {
        return "SHOW DATABASES";
    }

    @Override
    public String sample() {
        return null;
    }

    @Override
    public String dropTable(String tableName) {
        return "drop table if exists " + tableName;
    }
}
