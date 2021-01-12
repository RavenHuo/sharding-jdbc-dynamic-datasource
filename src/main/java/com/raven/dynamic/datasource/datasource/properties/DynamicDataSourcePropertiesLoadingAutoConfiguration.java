package com.raven.dynamic.datasource.datasource.properties;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.config.AbstractDynamicDataSource;
import com.raven.dynamic.datasource.config.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 15:27
 */
@Component
@ConditionalOnProperty(name = DynamicSourceConstant.DYNAMIC_IMPL_TYPE, havingValue = DynamicSourceConstant.DYNAMIC_PROPERTIES_IMPL_TYPE)
@EnableConfigurationProperties(PropertiesDataSourceConfigProperties.class)
@Slf4j
public class DynamicDataSourcePropertiesLoadingAutoConfiguration extends AbstractDynamicDataSource {


    @Autowired
    private PropertiesDataSourceConfigProperties propertiesDataSourceConfigProperties;

    @Value("${dynamic.datasource.className:com.zaxxer.hikari.HikariDataSource}")
    private String datasourceClassName;

    @Override
    @PostConstruct
    public void init() throws ClassNotFoundException{
        loadDataSource(propertiesDataSourceConfigProperties.getDatasource(), datasourceClassName);
    }

    /**
     * 加载 数据源
     *
     * @return
     */
    @Override
    public List<DynamicDataSourceProperties> loadDataSourceProperties(List<?> dynamicDataSourceProperties) {
        log.info("load  DataSourceProperties from properties-------------------");
        List<DynamicDataSourceProperties> result = new ArrayList<>();
        dynamicDataSourceProperties.stream().forEach(a -> {
            result.add((DynamicDataSourceProperties) a);
        });
        return result;
    }


}
