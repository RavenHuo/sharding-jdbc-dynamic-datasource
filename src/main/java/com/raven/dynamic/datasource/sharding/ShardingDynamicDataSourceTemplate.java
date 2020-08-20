package com.raven.dynamic.datasource.sharding;

import com.raven.dynamic.datasource.config.DynamicDataSourceTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: huorw
 * @create: 2020-08-19 22:53
 */
public interface ShardingDynamicDataSourceTemplate extends DynamicDataSourceTemplate {

    List<String> SHARDING_TABLE = new ArrayList<>();
}
