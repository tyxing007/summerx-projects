# Framework

## 核心组件 ##

## 配置管理 ##

分布式配置管理平台：百度disconf，淘宝diamond，360 qconf总有一款适合你。

## 1. [百度disconf](https://github.com/knightliao/disconf) ![](https://raw.githubusercontent.com/summerxyg/summerxyg.github.io/master/images/recommend%20.gif)

- [ZooKeeper安装](https://zookeeper.apache.org/)
 - 下载zookeeper压缩包，解压
 - 配置zoo.cfg
 - 在dataDir目录下创建myid，内容为server.A中的A，表明是那个server
```
cd /opt/app/
tar -zxvf zookeeper-3.4.6.tar.gz
cd zookeeper-3.4.6
cp conf/zoo_sample.cfg conf/zoo.cfg
vi conf/zoo.cfg
sh zkServer.sh start
```
> **配置参数**
ticketTime  : 心跳间隔（毫秒）
initLimit   :slave初始化连接到master的最大忍受时间（n个心跳间隔）
syncLimit   ：master与slave间同步最大限制时间（n个心跳间隔）
dataDir     ：存储目录
clientPort  ：客户端连接到Zookeeper服务器的端口
server.A    ： 第n个服务器，ip:port:port, 第一个端口用来集群成员的信息交换，第二个端口是在leader挂掉时专门用来进行选举leader所用。

- [disconf部署](https://github.com/knightliao/disconf/tree/master/disconf-web)

## SOA治理 ##

## 缓存管理 ##

## 1. [Memcache](https://github.com/memcached/memcached)

- 安装

## 工作调度 ##

## 6. 日志与监控


