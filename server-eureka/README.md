# (一)服务注册中心

### Eureka
Eureka包含了服务器端和客户端组件。服务器端，也被称作是服务注册中心，用于提供服务的注册与发现


### server-eureka搭建

*  创建子模块server-eureka，其pom.xml继承了父pom文件，引入spring-cloud-starter-netflix-eureka-server的依赖

```
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>
```

* 启动一个服务注册中心，只需要一个注解@EnableEurekaServer，这个注解需要在springboot工程的启动application类上加：

```java
@SpringBootApplication
@EnableEurekaServer
public class ServerEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerEurekaApplication.class, args);
    }
}
```

* 在默认配置下，Eureka Server会将自己也作为客户端来尝试注册自己，我们需要禁用它的客户端禁用行为.
通过eureka.client.registerWithEureka：false和fetchRegistry：false来表明自己是一个eureka server.
application.yml中配置eureka

```
server:
  port: 8061    #服务注册中心端口号

spring:
  application:
    name: server-eureka

eureka:
  instance:
    hostname: localhost    #服务注册中心实例的主机名
  client:
    register-with-eureka: false    #是否向服务注册中心注册自己
    fetch-registry: false    #是否检索服务
    service-url:
      defaultZone: http://${eureka.instance.hostname}.${server.port}/eureka/    #服务注册中心的配置内容，指定服务注册中心的位置
```