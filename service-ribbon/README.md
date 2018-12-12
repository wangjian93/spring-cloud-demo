# (三) 服务消费者（rest+ribbon）

目前，在Spring cloud 中服务之间通过restful方式调用有两种方式
- restTemplate+Ribbon
- feign

### ribbon
Ribbon是Netflix发布的负载均衡器，它有助于控制HTTP和TCP的客户端的行为。为Ribbon配置服务提供者地址后，Ribbon就可基于某种负载均衡算法，
自动地帮助服务消费者去请求。Ribbon默认为我们提供了很多负载均衡算法，例如轮询、随机等。当然，我们也可为Ribbon实现自定义的负载均衡算法。
在Spring Cloud中，当Ribbon与Eureka配合使用时，Ribbon可自动从Eureka Server获取服务提供者地址列表，并基于负载均衡算法，请求其中一个
服务提供者实例.

### service-ribbon搭建

* 跟service-hi引入相同的依赖，构建client，同样在启动时会向eureka server 注册
* 引入ribbon依赖

```
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    </dependency>
```

* yml配置

```
server:
  port: 8064

spring:
  application:
    name: service-ribbon

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

* 在工程的启动类中,通过@EnableEurekaClient向服务中心注册；并且向程序的ioc注入一个bean: restTemplate;
并通过@LoadBalanced注解表明这个restRemplate开启负载均衡的功能。

```java
@SpringBootApplication
@EnableEurekaClient
public class ServiceRibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

* 写一个测试类HelloService，通过之前注入ioc容器的restTemplate来消费service-hi服务的“/hi”接口，
在这里我们直接用的程序名替代了具体的url地址，在ribbon中它会根据服务名来选择具体的服务实例，
根据服务实例在请求的时候会用具体的url替换掉服务名.

```java
@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    public String helloService(String name) {
        return restTemplate.getForObject("http://SERVICE-HI/hi?name="+name, String.class);
    }
}
```

