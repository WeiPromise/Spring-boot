package com.promise.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Project Name:com.mininglamp.dataasset.config
 * Date:2019/3/11
 * Copyright (c) 2019, liuhaiyuan@mininglamp.com All Rights Reserved.
 */
@Component
@Slf4j
public class DBeans implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationCtx) throws BeansException {
        log.info("applicationContext:初始化");
        applicationContext = applicationCtx;
        log.info("applicationContext:初始化完成");
    }

    public synchronized static  <T> T bean(Class<T> clz) {
        return applicationContext.getBean(clz);
    }
}
