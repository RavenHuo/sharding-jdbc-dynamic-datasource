package com.raven.dynamic.datasource.sharding;

import com.raven.dynamic.datasource.config.DynamicDataSourceAfterRefresh;
import io.shardingsphere.api.ConfigMapContext;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.core.rule.ShardingRule;
import io.shardingsphere.shardingjdbc.jdbc.adapter.AbstractDataSourceAdapter;
import io.shardingsphere.shardingjdbc.jdbc.core.ShardingContext;
import io.shardingsphere.shardingjdbc.jdbc.core.connection.ShardingConnection;
import io.shardingsphere.transaction.api.TransactionTypeHolder;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * @description:
 * @author: huorw
 * @create: 2020-08-16 18:05
 */
@Slf4j
public class ShardingDynamicDataSource extends AbstractDataSourceAdapter implements ShardingDynamicDataSourceTemplate, DynamicDataSourceAfterRefresh {

    private ShardingContext shardingContext;

    private ShardingRule shardingRule;

    private Properties props;

    private ShardingRuleConfiguration shardingRuleConfig;


    public ShardingDynamicDataSource(final Map<String, DataSource> dataSourceMap, final ShardingRule shardingRule, final Map<String, Object> configMap, final Properties props) throws SQLException {
        super(dataSourceMap);
        if (!configMap.isEmpty()) {
            ConfigMapContext.getInstance().getConfigMap().putAll(configMap);
        }
        shardingContext = new ShardingContext(getDataSourceMap(), shardingRule, getDatabaseType(), props);
        this.shardingRule = shardingRule;
        this.props = props;
        this.shardingRuleConfig = shardingRule.getShardingRuleConfig();
    }

    public static ShardingDynamicDataSource createDataSource(
            final Map<String, DataSource> dataSourceMap, final ShardingRuleConfiguration shardingRuleConfig, final Map<String, Object> configMap, final Properties props) throws SQLException {
        return new ShardingDynamicDataSource(dataSourceMap, new ShardingRule(shardingRuleConfig, dataSourceMap.keySet()), configMap, props);
    }


    @Override
    public Connection getConnection() {
        return new ShardingConnection(getDynamicDataSource(), shardingContext, TransactionTypeHolder.get());
    }

    /**
     * 刷新方法,需再次初始化shardingRule及shardingContext
     *
     * @throws SQLException
     */
    @Override
    public void afterPropertiesSet() throws SQLException {
        Map<String, DataSource> dataMap = getDynamicDataSource();

        refreshShardingRule(dataMap);
        shardingRule = new ShardingRule(shardingRuleConfig, dataMap.keySet());

        shardingContext = new ShardingContext(dataMap, shardingRule, getDatabaseType(), props);
        refreshShardingTableName();
    }

    private void refreshShardingRule(Map<String, DataSource> dataMap) {
        shardingRuleConfig.getTableRuleConfigs().stream().forEach(s -> {
            StringBuffer actualDataNodes = new StringBuffer(s.getActualDataNodes());
            String[] nodes = actualDataNodes.toString().split("\\.", 2);

            if (nodes.length >= 2) {
                String defaultDbNodes = nodes[0];
                String actualTable = nodes[1];
                if (!dataMap.isEmpty()) {
                    dataMap.forEach((k, v) -> {
                        if (!k.equals(defaultDbNodes)) {
                            actualDataNodes.append(",").append(k).append(".").append(actualTable);
                        }
                    });
                }
            }
            s.setActualDataNodes(actualDataNodes.toString());
        });
    }

    /**
     * 刷新需要分表的表名
     */
    private void refreshShardingTableName() {
        SHARDING_TABLE.clear();
        shardingRuleConfig.getTableRuleConfigs().stream().forEach(s -> {
            SHARDING_TABLE.add(s.getLogicTable());
        });
    }
}
