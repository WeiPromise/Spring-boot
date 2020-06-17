package com.promise.jdbc.datasource;


import com.promise.jdbc.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

/**
 * Created by leiwei on 2019-6-18.
 */
public class Pool<T> {
    private HashMap<String, T> pool = new HashMap<String, T>();

    public synchronized T getInstance(Map<String, Object> properties, Function<Map<String, Object>, T> createFunc) {
        String key = JsonUtil.getStrFromObj(properties);
        if (!pool.containsKey(key))
            pool.put(key, createFunc.apply(properties));
        return pool.get(key);
    }

    public synchronized T getInstance(Properties properties, Function<Properties, T> createFunc) {
        String key = JsonUtil.getStrFromObj(properties);
        if (!pool.containsKey(key))
            pool.put(key, createFunc.apply(properties));
        return pool.get(key);
    }

    public void clearOldInstance(Map<String, Object> properties) {
        String key = JsonUtil.getStrFromObj(properties);
        pool.remove(key);
    }

    public void clearOldInstance(Properties properties) {
        String key = JsonUtil.getStrFromObj(properties);
        pool.remove(key);
    }
}
