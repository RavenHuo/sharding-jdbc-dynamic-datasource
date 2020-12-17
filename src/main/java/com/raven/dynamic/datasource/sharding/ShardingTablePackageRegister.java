package com.raven.dynamic.datasource.sharding;

import com.raven.dynamic.datasource.common.annotation.EnableSharding;
import com.raven.dynamic.datasource.common.annotation.ShardingTableEntity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @description:
 * @author: huorw
 * @create: 2020-08-03 21:40
 */
public class ShardingTablePackageRegister implements ImportBeanDefinitionRegistrar {

    /**
     * 注册 @ShardingTableEntity 的bean
     *
     * @param registry
     * @param packageNames
     */
    public static void register(BeanDefinitionRegistry registry, Collection<String> packageNames) {
        Assert.notNull(registry, "Registry must not be null");
        Assert.notNull(packageNames, "PackageNames must not be null");

        List<Class<?>> classList = com.raven.dynamic.datasource.common.utils.ClassUtils.getClasssFromPackage(StringUtils.toStringArray(packageNames)[0]);

        classList.stream().filter(s -> {
            return s.getAnnotation(ShardingTableEntity.class) != null;
        }).forEach(shardingTableEntity -> {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();

            beanDefinition.setBeanClassName(shardingTableEntity.getName());

            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            registry.registerBeanDefinition(shardingTableEntity.getName(), beanDefinition);
        });


    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        register(registry, getPackagesToScan(importingClassMetadata));
    }


    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(metadata.getAnnotationAttributes(EnableSharding.class.getName()));
        Set<String> packagesToScan = new LinkedHashSet<>();
        if (attributes != null) {
            String[] basePackages = attributes.getStringArray("basePackages");
            Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
            packagesToScan.addAll(Arrays.asList(basePackages));
            for (Class<?> basePackageClass : basePackageClasses) {
                packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
            }
        }
        if (packagesToScan.isEmpty()) {
            String packageName = ClassUtils.getPackageName(metadata.getClassName());
            Assert.state(!StringUtils.isEmpty(packageName), "@EnableSharding cannot be used with the default package");
            return Collections.singleton(packageName);
        }
        return packagesToScan;
    }


}
