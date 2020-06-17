package com.promise.jdbc.datasource;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Created by leiwei on 2019-6-18.
 */
@Builder
@Getter
public class MetaDB {
    private String[] dbName;

    public String[] concat(List<String> dbs) {
        dbName = new String[dbs.size()];
        for (int i=0; i<=dbs.size(); i++) {
            dbName[i] = dbs.get(i);
        }
        return dbName;
    }
}
