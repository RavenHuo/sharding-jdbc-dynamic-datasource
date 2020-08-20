package com.raven.dynamic.datasource.simple;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.config.DynamicDataSource;
import com.raven.dynamic.datasource.config.DynamicDataSourceTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @description:
 * @author: huorw
 * @create: 2020-08-14 23:05
 */
@Slf4j
@Component
@ConditionalOnProperty(name = DynamicSourceConstant.DYNAMIC_TYPE, havingValue = "simple", matchIfMissing = true)
public class SimpleDynamicDataSourceAutoConfiguration implements ApplicationRunner, DynamicDataSourceTemplate {

    private DynamicDataSource dynamicDataSource = new DynamicDataSource();

    @PostConstruct
    public void init() {
        log.info("SimpleDynamicDataSource loading------------------");
    }

    /**
     * 多数据源动态切换
     */
    @Bean(name = DynamicSourceConstant.DYNAMIC_DATASOURCE_ROUTING)
    @Primary
    public DynamicDataSource dynamicDataSourceRouting(@Qualifier(DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME) DataSource primaryDatasource) {
        dynamicDataSource.setDefaultTargetDataSource(primaryDatasource);
        dynamicDataSource.setTargetDataSources(DATA_SOURCE_MAP);
        dynamicDataSource.afterPropertiesSet();
        return dynamicDataSource;
    }


    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) {
        log.info("配置多数据源");
        dynamicDataSource.setTargetDataSources(DATA_SOURCE_MAP);
        dynamicDataSource.afterPropertiesSet();
    }

}
