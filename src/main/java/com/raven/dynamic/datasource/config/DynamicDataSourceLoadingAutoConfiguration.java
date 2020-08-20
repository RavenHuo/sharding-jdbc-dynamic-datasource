package com.raven.dynamic.datasource.config;

import com.raven.dynamic.datasource.datasource.database.DynamicDataSourceDataBaseLoadingAutoConfiguration;
import com.raven.dynamic.datasource.datasource.master2slave.DynamicDataSourceMasterSalveLoadingAutoConfiguration;
import com.raven.dynamic.datasource.datasource.properties.DynamicDataSourcePropertiesLoadingAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * @description:  动态加载数据源
 * @author: huorw
 * @create: 2020-05-21 16:28
 */
@Import({DynamicDataSourcePropertiesLoadingAutoConfiguration.class, DynamicDataSourceDataBaseLoadingAutoConfiguration.class, DynamicDataSourceMasterSalveLoadingAutoConfiguration.class})
@Slf4j
public class DynamicDataSourceLoadingAutoConfiguration {


    @PostConstruct
    public void init() {
        log.info("load DynamicDataSource init ----------------------------------");

    }



}
