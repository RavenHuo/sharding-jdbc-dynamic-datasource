package com.raven.dynamic.datasource.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 22:46
 */
public interface DynamicDataSourceFactory {

    String DEFAULT_DATASOURCE_CLASS_NAME = "com.zaxxer.hikari.HikariDataSource";

    @SuppressWarnings("unchecked")
    default  <T> T createDataSource(DataSourceProperties properties,
                                    Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }

    default Class<? extends DataSource> changeDataSourceClass(String datasourceClassName) throws ClassNotFoundException {
        if (StringUtils.isEmpty(datasourceClassName)) {
            datasourceClassName = DEFAULT_DATASOURCE_CLASS_NAME;
        }

        Class datasourceClass = Class.forName(datasourceClassName);

        return datasourceClass;
    }
}
