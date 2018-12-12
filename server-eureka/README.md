# (一)服务注册中心

### Eureka
EureKa采用C-S的设计架构，即包括了Eureka Server（服务端），EureKa client（客户端）。服务器端，也被称作是服务注册中心。

1. EureKa Server 提供服务注册，各个节点启动后，在EureKa server中进行注册；

2. EureKa Client 是一个Java客户端，用于和服务端进行交互，同时客户端也是一个内置的默认使用轮询负载均衡算法的负载均衡器。
在应用启动后，会向Eureka Server发送心跳（默认30秒）。如果EUR额卡 Server在多个心跳周期内没有接受到某个节点的心跳，
EureKa Server将会从服务注册表中将这个服务移出（默认90秒）。


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
    register-with-eureka: false    #关闭默认注册到eureka服务中心（该项目本身就是服务中心，无需自己注册自己）
    fetch-registry: false    #关闭自动抓取服务端，该工程本身就是服务端
    service-url:
      defaultZone: http://${eureka.instance.hostname}.${server.port}/eureka/    #服务注册中心的配置内容，指定服务注册中心的位置
```

### 访问注册中心时，界面上显示了红色粗体警告?

启动两个client，过了一会，停了其中一个，访问注册中心时，界面上显示了红色粗体警告信息。这表示eureka进入了保护模式。

>EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND
 HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.

原因分析：
这个是Eureka的自我保护机制。Eureka Server在运行期间，会统计心跳失败的比例在15分钟之内是否低于85%，如果出现低于的情况（在单机调试的时候很
容易满足，实际在生产环境上通常是由于网络不稳定导致），Eureka Server会将当前的实例注册信息保护起来，同时提示这个警告。

stackoverflow上，有人给出的建议是： 
1. 在生产上可以开自注册，部署两个server 
2. 在本机器上测试的时候，可以把比值调低，比如0.49 
3. 或者简单粗暴把自我保护模式关闭

```
eureka.server.enable-Self-Preservation: false
```

### Eureka的自我保护机制
在某一些时候注册在Eureka的服务已经挂掉了，但是服务却还留在Eureka的服务列表的情况。

##### Eureka服务端

默认情况下，如果Eureka Server在一定时间内（默认90秒）没有接收到某个微服务实例的心跳，Eureka Server将会移除该实例。
但是当网络分区故障发生时，微服务与Eureka Server之间无法正常通信，而微服务本身是正常运行的，此时不应该移除这个微服务，所以引入了自我保护机制。

自我保护模式正是一种针对网络异常波动的安全保护措施，使用自我保护模式能使Eureka集群更加的健壮、稳定的运行。


心跳监测机制：
Eureka server和client之间每隔30秒会进行一次心跳通信，告诉server，client还活着。由此引出两个名词： 
Renews threshold：server期望在每分钟中收到的心跳次数 
Renews (last min)：上一分钟内收到的心跳次数。

禁止注册server自己为client，不管server是否禁止，阈值（threshold）是1。client个数为n，阈值为1+2*n（此为一个server且禁止自注册的情况） 
如果是多个server，且开启了自注册，那么就和client一样，是对于其他的server来说就是client，是要*2的

自我保护机制的工作机制是如果在15分钟内超过85%的客户端节点都没有正常的心跳（Eurake有一个配置参数eureka.server.renewalPercentThreshold，
定义了renews 和renews threshold的比值，默认值为0.85，当server在15分钟内，比值低于percent，即少了15%的微服务心跳），
那么Eureka就认为客户端与注册中心出现了网络故障，
Eureka Server自动进入自我保护机制，此时会出现以下几种情况：

1. Eureka Server不再从注册列表中移除因为长时间没收到心跳而应该过期的服务。

2. Eureka Server仍然能够接受新服务的注册和查询请求，但是不会被同步到其它节点上，保证当前节点依然可用。

3. 当网络稳定时，当前Eureka Server新的注册信息会被同步到其它节点中。

```
# 该配置可以移除这种自我保护机制，防止失效的服务也被一直访问 (Spring Cloud默认该配置是 true) 
eureka.server.enable-self-preservation: false 

# 该配置可以修改检查失效服务的时间，每隔10s检查失效服务，并移除列表 (Spring Cloud默认该配置是 60s) 
eureka.server.eviction-interval-timer-in-ms: 10
```

##### Eureka客户端

```
# 该配置指示eureka客户端需要向eureka服务器发送心跳的频率  (Spring Cloud默认该配置是 30s) 
eureka.instance.lease-renewal-interval-in-seconds: 10 

# 该配置指示eureka服务器在接收到最后一个心跳之后等待的时间，然后才能从列表中删除此实例 (Spring Cloud默认该配置是 90s) 
eureka.instance.lease-expiration-duration-in-seconds: 30
```