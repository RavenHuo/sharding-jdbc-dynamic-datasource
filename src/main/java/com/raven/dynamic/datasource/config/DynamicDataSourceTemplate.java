package com.raven.dynamic.datasource.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 22:46
 */
public interface DynamicDataSourceTemplate {

    Map<Object, Object> DATA_SOURCE_MAP = new ConcurrentHashMap<>();

    /**
     * 初始化数据源
     * @param properties
     * @param type
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    default  <T> T createDataSource(DataSourceProperties properties,
                                            Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }

    /**
     * 获取配置的数据源
     * @return
     */
    default Map<String, DataSource> getDynamicDataSource() {
        Map<String, DataSource> dynamicDataSourceMap = new ConcurrentHashMap<>(8);
        DATA_SOURCE_MAP.forEach((k, v) -> {
            dynamicDataSourceMap.put((String) k, (DataSource) v);
        });
        return dynamicDataSourceMap;
    }

    /**
     * 添加默认数据源
     * @param defaultDataSource
     */
    default void addDefaultDataSource(DataSource defaultDataSource) {
        DATA_SOURCE_MAP.put("default", defaultDataSource);
    }
}
