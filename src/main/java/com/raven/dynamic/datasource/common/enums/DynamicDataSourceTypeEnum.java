package com.raven.dynamic.datasource.common.enums;

/**
 * @description: 加载数据源的方式
 * @author: huorw
 * @create: 2020-08-14 17:17
 */
public enum DynamicDataSourceTypeEnum {
    DYNAMIC_PROPERTIES_IMPL_TYPE("properties", "yml配置"),
    DATABASE("database", "数据库配置"),
    MASTER_SALVE("master-slave", "主从配置");

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String code;

    private String value;

    private DynamicDataSourceTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
