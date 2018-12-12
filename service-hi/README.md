# （二）服务提供者(eureka client)

### EureKa Client

EureKa Client 是一个Java客户端，用于和服务端进行交互，同时客户端也是一个内置的默认使用轮询负载均衡算法的负载均衡器。
在应用启动后，会向Eueka Server发送心跳（默认30秒）。如果EUR额卡 Server在多个心跳周期内没有接受到某个节点的心跳，
EureKa Server将会从服务注册表中将这个服务移出（默认90秒）。

EureKa Client会默认每30秒向server发送一次心跳，每30秒从server拉取一次服务列表维护在本地。


### EureKa Client搭建
* 客户端需要引入依赖

```
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
    </dependencies>
```

* 通过注解@EnableEurekaClient 表明自己是一个eurekaclient.

```java
@SpringBootApplication
@EnableEurekaClient
public class ServiceHiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHiApplication.class, args);
    }
}
```

* application.yml配置中指定服务注册中心

```
server:
  port: 8062

spring:
  application:
    name: service-hi    #服务名称

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8061/eureka/   #指定服务注册中心
```

### @EnableDiscoveryClient与@EnableEurekaClient区别

spring cloud中discovery service有许多种实现（eureka、consul、zookeeper等等），@EnableDiscoveryClient基于spring-cloud-commons,
@EnableEurekaClient基于spring-cloud-netflix。
其实用更简单的话来说，就是如果选用的注册中心是eureka，那么就推荐@EnableEurekaClient，如果是其他的注册中心，那么推荐使用@EnableDiscoveryClient。
