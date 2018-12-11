# （二）服务提供者(eureka client)

当client向server注册时，它会提供一些元数据，例如主机和端口，URL，主页等。Eureka server 从每个client实例接收心跳消息。 如果心跳超时，
则通常将该实例从注册server中删除。

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
