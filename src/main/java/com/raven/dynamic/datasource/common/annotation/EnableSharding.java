package com.raven.dynamic.datasource.common.annotation;


import com.raven.dynamic.datasource.sharding.ShardingTablePackageRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author raven
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(ShardingTablePackageRegister.class)
public @interface EnableSharding {

    /**
     * 实体类扫描包
     * @return
     */
    String[] basePackages() default {};


    Class<?>[] basePackageClasses() default {};
}
