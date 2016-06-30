package io.summerx.framework.zookeeper.curator;

/**
 * Created by xiayg on 6/30/2016.
 */
public class ZooKeeperTest {

    CuratorZooKeeperClient client;

    public void setup() throws Exception {
        // 创建一个zkclient
        client = new CuratorZooKeeperClient();
        client.setConnectString("172.21.129.63:2181,172.21.129.63:2182");
        client.afterPropertiesSet();

        client.remove("/conf/comm/jdbc.url");
        client.remove("/conf/comm/jdbc.username");
        client.remove("/conf/comm/jdbc.password");
        client.remove("/conf/app1/jdbc.url");
        client.remove("/conf/app1/jdbc.username");
        client.remove("/conf/app1/jdbc.password");

        client.put("/conf/comm/jdbc.url", "url@zk:/conf/comm");
        client.put("/conf/comm/jdbc.username", "username@zk:/conf/comm");
        client.put("/conf/app1/jdbc.url", "url@zk:/conf/app1");
    }
}
