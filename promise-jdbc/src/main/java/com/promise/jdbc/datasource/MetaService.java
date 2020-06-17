package com.promise.jdbc.datasource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leiwei on 2019-6-18.
 */
public interface MetaService {

    /**
     * @return 数据名称list
     */
    List<String> tableNames() throws SQLException;

    /**
     * @return 数据库名称list
     */
    List<String> dbNames() throws SQLException;

    /**
     * 测试数据源是否连通
     * @return
     */
    boolean test() throws SQLException;

    String getOwner();

    boolean dropTable(String tableName) throws SQLException;
}
