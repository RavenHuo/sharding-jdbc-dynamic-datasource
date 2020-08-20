package com.raven.dynamic.datasource.config.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-21 16:59
 */
@Slf4j
public class LocalDynamicDataSourceHolder {

    private LocalDynamicDataSourceHolder() {}

    private static ThreadLocal<String> contextHolder = new TransmittableThreadLocal<>();

    public static void setDbTag(String dbType) {
        log.info("switch dbTage ={}", dbType);
        contextHolder.set(dbType);
        DynamicHintManagerHolder.refreshHintManager(dbType);

    }

    public static String getDbTag() {
        return contextHolder.get();
    }

    public static void clearDbTag() {
        log.info("清除  数据源线程变量");
        contextHolder.remove();
        DynamicHintManagerHolder.clearHintManager();
    }
}
