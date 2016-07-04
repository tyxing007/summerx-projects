# Framework

## 核心组件 ##

## 配置管理 ##

分布式配置管理平台：百度disconf，淘宝diamond，360 qconf总有一款适合你。

## 1. [百度disconf](https://github.com/knightliao/disconf) ![](https://raw.githubusercontent.com/summerxyg/summerxyg.github.io/master/images/recommend%20.gif)

- [ZooKeeper安装与配置](https://zookeeper.apache.org/)
 - 下载zookeeper压缩包，解压
 - 配置zoo.cfg
 - 在dataDir目录下创建myid文件，内容为server.A中的A，表明是哪个server
```
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

  Memcached 是一个高性能的分布式内存对象缓存系统，用于动态Web应用以减轻数据库负载。它通过在内存中缓存数据和对象来减少读取数据库的次数，从而提供动态、数据库驱动网站的速度

- 安装与配置

 - libevent缺失，则先安装libevent
 - 下载
 - 编译
 - 启动
```
// 安装libevent依赖，如果已安装则跳过此步骤
sudo yum install libevent libevent-devel

// 安装memcache
wget http://memcached.org/latest
tar -zxvf memcached-1.4.27.tar.gz
cd memcached-1.4.27
./configure && make && make test && sudo make install
memcached -d -m 64 -p 11211
```

## 2. [Redis](https://github.com/antirez/redis)
  Redis是一个key-value存储系统。和Memcached类似，它支持存储的value类型相对更多，包括string(字符串)、 list(链表)、set(集合)和zset(有序集合)。这些数据类型都支持push/pop、add/remove及取交集并集和差集及更丰富的操作，而且这些操作都是原子性的。在此基础上，redis支持各种不同方式的排序。与memcached一样，为了保证效率，数据都是缓存在内存中。区别的是redis会周期性的把更新的数据写入磁盘或者把修改操作写入追加的记录文件，并且在此基础上实现了master-slave(主从)同步,当前 Redis的应用已经非常广泛

- 安装
 * 下载redis压缩包，解压
 * 编译
 * [配置](http://www.cnblogs.com/wenanry/archive/2012/02/26/2368398.html)
 * 启动
```
// 下载 & 解压 & 编译
wget -P /opt/install http://download.redis.io/releases/redis-3.2.1.tar.gz
tar -zxvf /opt/install/redis-3.2.1.tar.gz -C /opt/app
cd /opt/app/redis-3.2.1
make & make install
// 修改sysctl.conf
su root
echo vm.overcommit_memory=1 >> /etc/sysctl.conf
exit
// 创建node6379，注意这里省略了配置的修改，请根据实际情况修改配置
mkdir /opt/app/redis-cluster
mkdir /opt/app/redis-cluster/redis-node6379
mkdir /opt/app/redis-cluster/redis-node6379/data
cp /opt/app/redis-3.2.1/redis.conf /opt/app/redis-cluster/redis-node6379/redis.conf
redis-server /opt/app/redis-cluster/redis-node6379/redis.conf
// 其他节点同node6379

yum -y install ruby ruby-devel rubygems rpm-build
gem install redis
redis-trib.rb create --replicas 1 127.0.0.1:6379 127.0.0.1:6380
```
> **根据实际情况修改以下配置**
port 6379 ### 端口
dir /opt/app/redis-cluster/redis-node6379/data
daemonize yes ### redis默认不是后台启动，这里修改成后台启动
cluster-enabled yes ### 允许redis支持集群模式
cluster-config-file redis-node6379.conf ### 节点配置文件
cluster-node-timeout 15000 ###节点超时毫秒
appendonly yes


## 工作调度 ##

## 6. 日志与监控


