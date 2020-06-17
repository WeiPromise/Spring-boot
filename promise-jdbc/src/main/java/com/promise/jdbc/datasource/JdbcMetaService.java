package com.promise.jdbc.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.promise.jdbc.vendor.DMLProvider;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leiwei on 2019-6-18.
 */
@Getter
public class JdbcMetaService  implements MetaService {

    private static Pool<JdbcMetaService> metaServicePool = new Pool<>();
    private DruidPooledConnection conn;
    private DMLProvider dmlProvider;
    private Statement stmt;
    private String owner;

    private JdbcMetaService(Map<String, Object> properties) {
        try {
            DruidDataSource ds = new DruidDataSource();
            ds.setDriverClassName(properties.get("vendor").toString());
            ds.setUrl(properties.get("url").toString());
            ds.setUsername(properties.get("username").toString());
            ds.setPassword(properties.get("password").toString());
            ds.setInitialSize(1);
            ds.setMinIdle(1);
            ds.setMaxActive(50);
            ds.setMaxWait(300000);// 5 min
            ds.setTestWhileIdle(true);
            ds.setConnectionErrorRetryAttempts(0);
            ds.setBreakAfterAcquireFailure(true);
            ds.setTimeBetweenEvictionRunsMillis(1800000);//30 min
            ds.setRemoveAbandoned(true); //连接长时间后强制关闭
            ds.setRemoveAbandonedTimeout(300);//5min
            ds.setValidationQuery("SELECT 1");
            //ds.setConnectionProperties("remarksReporting=true;useUnicode=true;characterEncoding=UTF-8");//从数据库中获得每个字段的注释
            ds.setConnectionProperties("remarksReporting=true");//从数据库中获得每个字段的注释
            conn = ds.getConnection();
            dmlProvider = (DMLProvider) Class.forName(properties.get("vendor").toString()).newInstance();
            stmt = conn.createStatement();
        } catch (Exception e) {
            throw new RuntimeException("RDBMS数据源创建失败!", e);
        }
    }

    public static JdbcMetaService get(String vendor, String url, String username, String password) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("vendor", vendor);
        properties.put("url", url);
        properties.put("username", username);
        properties.put("password", password);
        //每个线程一个conn
        //properties.put("thread-id", Thread.currentThread().getId());
        JdbcMetaService service = metaServicePool.getInstance(properties, JdbcMetaService::new);
        if (service.conn.isAbandonded()) {
            metaServicePool.clearOldInstance(properties);
            return metaServicePool.getInstance(properties, JdbcMetaService::new);
        }
        return service;
    }

    @Override
    public List<String> dbNames() throws SQLException {
        List<String> dbList = new ArrayList<>();
        String sql = dmlProvider.listDBs(getOwner());
        if (sql == null) {
            ResultSet rs = conn.getMetaData().getTables(null, getOwner(), null, new String[]{"TABLE", "VIEW"});
            while (rs.next()) {
                String dbName = rs.getString("TABLE_NAME");
                if (dbName != null) {
                    dbList.add(dbName);
                }
            }
        } else {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String dbName = rs.getString(1);
                if (dbName != null) {
                    dbList.add(dbName);
                }
            }
        }
        return dbList;
    }

    @Override
    public List<String> tableNames() throws SQLException {
        List<String> tableList = new ArrayList<>();
        String sql = dmlProvider.listTables(getOwner());
        if (sql == null) {
            ResultSet rs = conn.getMetaData().getTables(null, getOwner(), null, new String[]{"TABLE", "VIEW"});
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (tableName != null) {
                    tableList.add(tableName);
                }
            }
        } else {
            stmt.execute("USE "+ getOwner());
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tableName = rs.getString(1);
                if (tableName != null) {
                    tableList.add(tableName);
                }
            }
        }
        return tableList;
    }

    @Override
    public boolean test() throws SQLException {
        return !conn.isDisable();
    }

    public JdbcMetaService setOwner(String owner){
        this.owner = owner;
        return this;
    }

    @Override
    public String getOwner(){
        return this.owner;
    }

    @Override
    public boolean dropTable(String tableName) throws SQLException {
        if(!validateTableNameExist(tableName))
            return true;
        String sql = dmlProvider.dropTable(tableName);
        if (null == sql) {
            return false;
        } else {
            stmt.execute("USE "+ getOwner());
            stmt.execute(sql);
            return true;
        }
    }

    public boolean validateTableNameExist(String tableName) {
        boolean isExist = false;
        try {
            ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null);
            if (rs.next()) isExist = true;
            else isExist =  false;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return isExist;
    }

}
