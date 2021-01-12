package com.raven.dynamic.datasource.sharding;

import com.raven.dynamic.datasource.common.annotation.ShardingTableEntity;
import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.config.DynamicDataSourceTemplate;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.HintShardingStrategyConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.persistence.Table;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: huorw
 * @create: 2020-07-29 14:27
 */
@Slf4j
@Component
@ConditionalOnProperty(name = DynamicSourceConstant.DYNAMIC_TYPE, havingValue = "sharding")
public class ShardingDynamicDataSourceAutoConfig implements ApplicationRunner, DynamicDataSourceTemplate {

    private final ApplicationContext applicationContext;

    private final static String SQL_PROPERTY = "sql.show";

    private final static String CHECK_PROPERTY = "check.table.metadata.enabled";

    private ShardingDynamicDataSource shardingDataSource;

    public ShardingDynamicDataSourceAutoConfig(ApplicationContext applicationContext) {
        log.info("sharding loading--------------------");
        this.applicationContext = applicationContext;
    }

    @Bean(name = "shardingDataSource")
    @Primary
    public ShardingDynamicDataSource getDataSource(@Qualifier(DynamicSourceConstant.PRIMARY_DATASOURCE_BEAN_NAME) DataSource primaryDatasource) throws SQLException {
        log.info("shardingDataSource loading--------------------");
        shardingDataSource =  buildDataSource(primaryDatasource);
        return shardingDataSource;
    }

    private ShardingDynamicDataSource buildDataSource(DataSource primaryDatasource) throws SQLException {

        // 添加默认数据源
        addDefaultDataSource(primaryDatasource);

        //分库设置
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("default",primaryDatasource);

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();

        List<TableRuleConfiguration> ruleConfigurationList = new ArrayList<>();

        Map<String, Object> shardingTableBeanMap = getShardingEntityBean();


        shardingTableBeanMap.forEach((name, bean) -> {
            ShardingTableEntity shardingTable = bean.getClass().getAnnotation(ShardingTableEntity.class);
            Table table = bean.getClass().getAnnotation(Table.class);

            ruleConfigurationList.add(defaultTableRuleConfig(shardingTable, table));
        });

        shardingRuleConfig.setTableRuleConfigs(ruleConfigurationList);
        // 强制路由转换必须设置默认路由配置
        ShardingHintShardingAlgorithm shardingHintShardingAlgorithm = new ShardingHintShardingAlgorithm();
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new HintShardingStrategyConfiguration(shardingHintShardingAlgorithm));
        Properties properties = new Properties();
        properties.setProperty(SQL_PROPERTY, Boolean.TRUE.toString());
        properties.setProperty(CHECK_PROPERTY, Boolean.TRUE.toString());
        // 获取数据源对象
        return ShardingDynamicDataSource.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(1), properties);
    }

    /**
     * 表分片策略
     *
     * @return
     */
    private TableRuleConfiguration defaultTableRuleConfig(ShardingTableEntity shardingTable, Table table) {



        String key = shardingTable.key();
        // 分表的表达式
        String shardingTableExpression = shardingTable.shardingTableExpression();

        int shardingTableNum = shardingTable.shardingTableNum();


        String logicTable = table.name();
        String tableShardingStr = logicTable + "${0}";

        if (shardingTableNum > 1 ) {
            tableShardingStr = logicTable + "${0.." + (shardingTableNum - 1 ) + "}";
        }



        // 配置表规则
        TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration();
        // 逻辑表
        tableRuleConfig.setLogicTable(logicTable);

        //${} 是一个groovy表达式，[]表示枚举，{...}表示一个范围。
        //整个inline表达式最终会是一个笛卡尔积，表示ds_0.t_order_0. ds_0.t_order_1
        // ds_1.t_order_0. ds_1.t_order_0
        //真实表
        tableRuleConfig.setActualDataNodes("default."+tableShardingStr);
        tableRuleConfig.setKeyGeneratorColumnName(key);

        // 分表策略
        tableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration(key, logicTable + "${" + shardingTableExpression + "}"));
        return tableRuleConfig;
    }


    /**
     * 获取 ShardingTableEntity注解的bean
     *
     * @return
     */
    private Map<String, Object> getShardingEntityBean() {
        return applicationContext.getBeansWithAnnotation(ShardingTableEntity.class);
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        shardingDataSource.afterPropertiesSet();
    }
}
