package com.promise.jdbc.vendor;

import com.mysql.cj.jdbc.Driver;

import java.sql.SQLException;

/**
 * Created by leiwei on 2019-6-18.
 */
@VendorIdentifier(name = "Oracle")
//public class OracleVendor extends OracleDriver implements Vendor {
public class OracleVendor extends Driver implements Vendor {


    public OracleVendor() throws SQLException {}

    @Override
    public String listTables(String Owner) {
        return "SELECT TABLE_NAME FROM ALL_TABLES WHERE OWNER='" + Owner + "' UNION SELECT VIEW_NAME FROM ALL_VIEWS WHERE OWNER='" + Owner + "'";
    }

    @Override
    public String listDBs(String Owner) {
        return "null";
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
