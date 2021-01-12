package com.raven.dynamic.datasource.config.context;

import com.raven.dynamic.datasource.sharding.ShardingDynamicDataSourceTemplate;
import io.shardingsphere.api.HintManager;
import io.shardingsphere.core.hint.HintManagerHolder;

import java.util.List;

/**
 * @description:
 * @author: huorw
 * @create: 2020-08-19 22:46
 */
public class DynamicHintManagerHolder {

    public static void refreshHintManager(String dbType){
        HintManagerHolder.clear();
        HintManager hintManager = HintManager.getInstance();

        List<String> shardingTableName = ShardingDynamicDataSourceTemplate.SHARDING_TABLE;

        shardingTableName.stream().forEach(tableName-> {
            hintManager.addTableShardingValue(tableName,1);
            // 强制路由，切换数据源
            hintManager.addDatabaseShardingValue(tableName, dbType);
        });
    }

    public static void clearHintManager() {
        HintManager hintManager = HintManagerHolder.get();
        if (hintManager != null) {
            hintManager.close();
        }
    }
}
