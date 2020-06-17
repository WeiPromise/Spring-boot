package com.promise.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Project Name:com.mininglamp.dataasset.config
 * Date:2019/3/11
 * Copyright (c) 2019, liuhaiyuan@mininglamp.com All Rights Reserved.
 */
@Component
public class Env{
    private static Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        Env.environment = environment;
    }

    public static String getProperty(String key){
        return environment.getProperty(key);
    }
}