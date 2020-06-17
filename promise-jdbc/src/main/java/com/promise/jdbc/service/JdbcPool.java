package com.promise.jdbc.service;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leiwei on 2019-6-18.
 */
@Slf4j
public class JdbcPool {

    private static final Map<String, JdbcPool> bcdMap = new HashMap<>();

    public final DruidDataSource ds;

    private JdbcPool(String driverClass, String url, String username, String password) {
        ds = new DruidDataSource();
        ds.setDriverClassName(driverClass);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setInitialSize(1);
        ds.setMinIdle(1);
        ds.setMaxActive(20);
        ds.setMaxWait(300000);// 5 min
        ds.setTestWhileIdle(true);
        ds.setTimeBetweenEvictionRunsMillis(1800000);//30 min
        ds.setRemoveAbandoned(true); //连接长时间后强制关闭
        ds.setRemoveAbandonedTimeout(300);//5min
        //ds.setConnectionProperties("remarksReporting=true;useUnicode=true;characterEncoding=UTF-8");//从数据库中获得每个字段的注释
        ds.setConnectionProperties("remarksReporting=true");//从数据库中获得每个字段的注释
    }


    public static void main(String[] args) {

        JdbcPool jdbcPool = new JdbcPool("com.mysql.cj.jdbc.Driver", "jdbc:mysql://ws04.mlamp.cn:3306/display_gengzx", "root", "123456");

        try (final DruidPooledConnection connection = jdbcPool.ds.getConnection()) {
//            DatabaseMetaData db = connection.getMetaData();
//            ResultSet tables = db.getTables(null, "display_gengzx", "data_source_type", new String[]{"TABLE"});

            System.out.println(connection.isDisable());
//            String database = "display_gengzx";
//            String sql = "select `table_name`, `table_type`, `table_comment` from information_schema.tables where table_schema='display_gengzx' and table_type='base table';";
////            System.out.println(db.getSchemas(null, "display_gengzx").next());
//            ResultSet db = connection.prepareStatement(sql).executeQuery();
//
//            System.out.println(connection.isAbandonded());
//            System.out.println(jdbcPool.ds.getActiveCount());
//            System.out.println(jdbcPool.ds.getActivePeak());
//            while (db.next()) {
////                System.out.println("3table TABLE_NAME: "+db.getString("TABLE_NAME"));
//                System.out.println(db.getString(1));
//                break;
//            }
//
//            System.out.println(connection.isAbandonded());
//            System.out.println(jdbcPool.ds.getActiveCount());
//            System.out.println(jdbcPool.ds.getActivePeak());

//            ResultSet columns = db.getColumns(null, "display_gengzx".toUpperCase(), "data_source_type", "%");
//            while (columns.next()) {
//                System.out.println("column COLUMN_NAME: "+columns.getString("COLUMN_NAME"));
//                System.out.println("column TYPE_NAME: "+columns.getString("TYPE_NAME"));
//                System.out.println("column REMARKS: "+columns.getString("REMARKS"));
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
