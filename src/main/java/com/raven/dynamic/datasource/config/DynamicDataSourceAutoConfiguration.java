package com.raven.dynamic.datasource.config;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.sharding.ShardingDynamicDataSourceAutoConfig;
import com.raven.dynamic.datasource.simple.SimpleDynamicDataSourceAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * @description:
 * @author: huorw
 * @create: 2020-08-15 10:17
 */
@Configuration
@ComponentScan({"com.raven.dynamic.datasource.config"})
@ConditionalOnProperty(value = DynamicSourceConstant.DYNAMIC_DATASOURCE_SWITCH, havingValue = "true")
@Import({DynamicDataSourceLoadingAutoConfiguration.class, SimpleDynamicDataSourceAutoConfiguration.class, ShardingDynamicDataSourceAutoConfig.class})
@Slf4j
public class DynamicDataSourceAutoConfiguration {

    @PostConstruct
    public void init() {
        log.info("DynamicDataSourceAutoConfiguration init-------------------");
    }

}
