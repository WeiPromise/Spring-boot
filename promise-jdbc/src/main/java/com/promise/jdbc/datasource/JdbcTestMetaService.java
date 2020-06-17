package com.promise.jdbc.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import lombok.Getter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leiwei on 2019-6-18.
 */
@Getter
public class JdbcTestMetaService implements MetaService {

    private DruidPooledConnection conn;

    private JdbcTestMetaService(Map<String, Object> properties) throws SQLException {
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
    }

    public static JdbcTestMetaService get(String vendor, String url, String username, String password) throws SQLException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("vendor", vendor);
        properties.put("url", url);
        properties.put("username", username);
        properties.put("password", password);
        return new JdbcTestMetaService(properties);
    }


    @Override
    public List<String> tableNames() throws SQLException {
        return null;
    }

    @Override
    public List<String> dbNames() throws SQLException {
        return null;
    }

    @Override
    public boolean test() throws SQLException {
        boolean flag = conn.isDisable() == true ? false : true;
        if(flag == true)
            conn.close();
        return flag;
    }

    @Override
    public String getOwner() {
        return null;
    }

    @Override
    public boolean dropTable(String tableName) {
        return false;
    }


}
