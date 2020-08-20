package com.raven.dynamic.datasource.common.annotation;

import java.lang.annotation.*;

/**
 * 分表实体类
 * @date 2020-08-03
 * @author raven
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardingTableEntity {


    /**
     * 分表的字段
     * @return
     */
    String key() default "";

    /**
     * 分表的表达式，不填默认为 空
     * @return
     */
    String shardingTableExpression() default "";

    /**
     * 分表个数
     * @return
     */
    int shardingTableNum() default 1;

}
