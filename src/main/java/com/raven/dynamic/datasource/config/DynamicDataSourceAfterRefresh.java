package com.raven.dynamic.datasource.config;

import java.sql.SQLException;

/**
 * @description:
 * @author: huorw
 * @create: 2020-08-19 11:42
 */
public interface DynamicDataSourceAfterRefresh {

    /**
     * 刷新方法
     * @throws SQLException
     */
    void afterPropertiesSet() throws SQLException;
}
