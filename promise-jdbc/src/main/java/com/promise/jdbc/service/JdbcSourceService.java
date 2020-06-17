package com.promise.jdbc.service;


import com.promise.jdbc.datasource.JdbcMetaService;
import com.promise.jdbc.datasource.JdbcTestMetaService;
import com.promise.jdbc.datasource.MetaService;
import com.promise.jdbc.util.ClassFinder;
import com.promise.jdbc.util.DsmJdbcException;
import com.promise.jdbc.vendor.Vendor;
import com.promise.jdbc.vendor.VendorIdentifier;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by leiwei on 2019-6-18.
 */
public class JdbcSourceService {
    HashMap<String, Class> vendorMap;

    public JdbcSourceService() {
        init();
    }

    private void init() {
        vendorMap = new HashMap<>();
        for (Class<?> clazz : ClassFinder.getClasses("com.promise.jdbc.vendor")) {
            if (clazz.isAnnotationPresent(VendorIdentifier.class) && Vendor.class.isAssignableFrom(clazz)) {
                VendorIdentifier vendor = clazz.getDeclaredAnnotation(VendorIdentifier.class);
                vendorMap.put(vendor.name(), clazz);
                System.out.println(vendor.name() + "  " + clazz.getName());
            }
        }
    }

    public JdbcMetaService dsmMetaService(String sourceType, String url, String username, String password) {
        String vendor = vendorMap.get(sourceType).getName();
        try {
            return JdbcMetaService.get(vendor, url, username, password);
        } catch (Exception e) {
            throw new DsmJdbcException("JdbcMetaService connect failed!", e);
        }
    }

    public JdbcTestMetaService dsmTestMetaService(String sourceType, String url, String username, String password) throws SQLException {
        String vendor = vendorMap.get(sourceType).getName();
        return JdbcTestMetaService.get(vendor, url, username, password);
    }

    public static void main(String args[]) {
        MetaService metaService = new JdbcSourceService().dsmMetaService("Mysql", "jdbc:mysql://ws04.mlamp.cn:3306/display_gengzx", "root", "123456").setOwner("display_gengzx");
        try {
            for (String s : metaService.tableNames()) {
                System.out.println(s);
            }
        } catch (Exception e) {
            throw new DsmJdbcException("JdbcMetaService connect failed!", e);
        }
    }
}
