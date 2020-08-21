## sharding-dynamic-datasource多数据源切换。
基于springBoot及sharding-jdbc 的分表 多数据源切换

是dynamic-datasource的升级版


- [项目下载运行](#项目下载运行)
- [dynamic-datasource支持的配置项](#sharding-dynamic-datasource支持的配置项)
- [开启配置](#开启配置)
- [配置多数据源](#配置多数据源)
    - [配置文件方式](#配置文件方式) 
    - [数据库方式](#数据库方式)
    - [主从读写数据源](#主从读写数据源)
- [配置分库分表](#配置分库分表)
       


## <p id="项目下载运行"> 项目下载运行

```
git clone git@github.com:RavenHuo/sharding-jdbc-dynamic-datasource.git
cd ./dynamic-datasource
mvn clean install -Dmaven.test.skip=true
```

在spring项目中引入maven依赖
```
<dependency>
    <groupId>com.raven.dynamic.datasource</groupId>
    <artifactId>sharding-jdbc-dynamic-datasource</artifactId>
    <version>0.0.2</version>
</dependency>
```

### <p id="dynamic-datasource支持的配置项"> dynamic-datasource支持的配置项

- 1、支持以配置文件方式，配置多数据源
- 2、支持数据库（默认数据源），配置多数据源
- 3、支持主从读写切换数据源
- 4、基于header的数据源切换
- 5、基于url前缀的数据源切换
- 6、基于注解的数据源切换（默认支持）


## <p id="开启配置">开启配置

```
dynamic:
  datasource:
    enable: true
```



## <p id="配置多数据源">配置多数据源
### <p id="配置文件方式"> 1、配置文件方式
在.yml配置文件中添加以下配置。
```
dynamic:
  impl:
    type: properites
  datasource:
    enable: true

    dataSource[0]:
      dataSourceTag: 'db0'
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306
      username: username
      password: password
    
    dataSource[1]:
      dataSourceTag: 'db1'
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3307
      username: username
      password: password

```

### <p id="数据库方式">2、数据库方式

```
dynamic:
  impl:
    type: database
  datasource:
    enable: true
```

并在默认数据源中执行以下sql
```
CREATE TABLE `t_demo` (
`id` VARCHAR ( 40 ) NOT NULL COMMENT 'id',
`status` int(11) NOT NULL DEFAULT '1' COMMENT '状态',
`data_source_tag` VARCHAR ( 64 ) NOT NULL COMMENT '数据源标识',
`driver_class_name` VARCHAR ( 128 ) NOT NULL COMMENT '数据源驱动类名',
`url` VARCHAR ( 128 ) NOT NULL COMMENT '数据源链接地址',
`username` VARCHAR ( 64 ) NOT NULL COMMENT '数据库连接用户名',
`password` VARCHAR ( 64 ) NOT NULL COMMENT '数据库连接密码',
PRIMARY KEY ( `id` ) USING BTREE 
) ENGINE = INNODB DEFAULT CHARSET = utf8;
```
### <p id="主从读写数据源">3、主从读写数据源

```
dynamic:
  impl:
    type: master-slave
    master:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306
      username: username
      password: password
  
    slave:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3307
      username: username
      password: password
```

## <p id="配置分库分表">配置分库分表
目前只支持jpa（因为工作中常用。。）
```
dynamic:
  type: sharding
```
然后在SpringApplication启动文件添加注解@EnableSharding

最后在需要分表的Entity上使用@ShardingTableEntity
```
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
```
最后分别配置@ShardingTableEntity中的三个字段
