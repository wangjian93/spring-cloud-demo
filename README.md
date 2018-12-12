# Spring-Cloud-demo
微服务是未来的趋势，本项目旨在演示Spring Cloud的使用，为学习而搭建。

### Spring Cloud是什么？

Spring Cloud按照官方的说法它是一套完整的微服务解决方案，是基于Spring Boot框架的。Spring Cloud 并不重复造轮子，而是将市面一些开发的比较
好的微服务框架，集成进去，通过Spring Boot进行封装，从而减少了各模块的开发成本。Spring Cloud 提供了构建分布式系统所需的“全家桶”。

### Spring Cloud 与 Dubbo对比
Dubbo是阿里巴巴SOA服务化治理方案的核心框架，也是目前比较流行的微服务架构方案。Dubbo只是实现了服务治理，而Spring Cloud子项目分别覆盖了微
服务架构下的众多部件，而服务治理只是其中的一个方面。

| 核心要素 | Dubbo | Spring Cloud |
| ------ | ------ | ------ |
| 服务注册中心 | Zookeeper、Redis | Spring Cloud Netflix Eureka |
| 服务调用方式 | RPC | REST API |
| 服务网关 | 无 | Spring Cloud Netflix Zuul |
| 断路器 | 不完善 |  Spring Cloud Netflix Hystrix |
| 分布式配置 | 无 | Spring Cloud Config |
| 分布式追踪系统 | 无 | Spring Cloud Sleuth |
| 消息总线 | 无 | Spring Cloud Bus |
| 数据流 | 无 | Spring Cloud Stream 基于Redis、Rabbit、Kafka实现的消息微服务 |
| 批量任务 | 无 | Spring Cloud Task |


### 项目模块
* spring-cloud-demo
    * server-eureka(端口：8061)：服务注册中心
    * service-hi(端口：8062、8063)：服务提供者
    * service-ribbon(端口：8064)：服务消费者（基于rest+ribbon）
    
### spring-cloud-demo搭建
本项目是maven的多模块项目。
首先创建主Maven工程，在其pom文件引入依赖，spring Boot版本为2.0.4.RELEASE，Spring Cloud版本为Finchley.RELEASE。
这个pom文件作为父pom文件，起到依赖版本控制的作用，其他module工程继承该pom。

* 引入Spring Boot模块
    
```
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.4.RELEASE</version>
    </parent>
```

* 引入Spring Cloud模块

```
    <dependencyManagement>
        <dependencies>
            <!-- Spring Cloud 依赖 -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```
