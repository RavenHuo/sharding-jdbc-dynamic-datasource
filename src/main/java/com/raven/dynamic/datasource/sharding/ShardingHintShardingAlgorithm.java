package com.raven.dynamic.datasource.sharding;

import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @description:
 * @author: huorw
 * @create: 2020-08-14 16:57
 */
@Slf4j
public class ShardingHintShardingAlgorithm implements HintShardingAlgorithm {

    /**
     * 自定义Hint 实现算法
     * 能够保证绕过Sharding-JDBC SQL解析过程
     * @param availableTargetNames
     * @param shardingValue 不再从SQL 解析中获取值，而是直接通过下面代码参数指定
     *         HintManager hintManager = HintManager.getInstance();
     *         hintManager.setDatabaseShardingValue("ds_exchange");
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ShardingValue shardingValue) {

        log.info("shardingValue={}" , shardingValue.getColumnName());
        log.info("availableTargetNames={}" , availableTargetNames);
        List<String> shardingResult = new ArrayList<>();
        ListShardingValue<String> tmpSharding = (ListShardingValue<String>) shardingValue;
        for(String value : tmpSharding.getValues()){
            if(availableTargetNames.contains(value)) {
                shardingResult.add(value);
            }
        }
        return shardingResult;
    }
}