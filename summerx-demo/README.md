# DEMO

一个演示程序

---

## 架构

项目使用gradle构建, 包含但不限于以下技术

* [springframework](https://github.com/spring-projects/spring-framework)
* [dubbox](https://github.com/dangdangdotcom/dubbox)
* [Hiberate](https://github.com/hibernate/hibernate-orm)
* [ActiveMQ](https://github.com/apache/activemq)
* [ZooKeeper](https://github.com/apache/zookeeper)
* [MongoDB]
* [Memcache/Redis]

## 安装

- [Gradle](https://github.com/gradle/gradle)

- [ZooKeeper](https://zookeeper.apache.org/) (依赖jdk1.6+)
  * 注意在dataDir目录下创建myid，内容为server.A中的A，表明是那个server
```
cd /opt/app/
tar -zxvf zookeeper-3.4.6.tar.gz
cd zookeeper-3.4.6
cp conf/zoo_sample.cfg conf/zoo.cfg
vi conf/zoo.cfg     -- 配置zoo.cfg
sh zkServer.sh start
```
配置zoo.cfg
>
ticketTime  : 心跳间隔（毫秒）
initLimit   ：slave初始化连接到master的最大忍受时间（n个心跳间隔）
syncLimit   ：master与slave间同步最大限制时间（n个心跳间隔）
dataDir     ：存储目录
clientPort  ：客户端连接到Zookeeper服务器的端口
server.A    ： 第n个服务器，ip:port:port, 第一个端口用来集群成员的信息交换，第二个端口是在leader挂掉时专门用来进行选举leader所用。


## Quickstart

- import into eclipse
```
gradle eclipse
File -> Import -> General -> Existing projects into workspace
Right Click -> Configure -> Convert to Gradle(STS) Project
```

- import into idea
```
File-> import project -> import from external model -> Gradle
```

- 启动dubbo服务提供者
```
cd infoccsp-booking-provider
gradle run
```
- 启动dubbo服务消费者
IDEA环境下使用jboss插件部署应用
Eclipse环境下使用gradle发布应用
```
修改gradle.properties的jbossHome
cd ../infoccsp-booking-webapp
gradle jbossDeploy
```
打开 http://localhost:8080/booking/demo/sayHello?name=Tom


## 开发

## 部署

## 开发注意事项

---

Tags : CCSP
