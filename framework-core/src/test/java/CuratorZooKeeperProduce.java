import io.summerx.framework.zookeeper.curator.CuratorZooKeeperClient;

/**
 * Created by xiayg on 6/29/2016.
 */
public class CuratorZooKeeperProduce {


    public static void main(String[] args) throws Exception {

        CuratorZooKeeperClient client = new CuratorZooKeeperClient();
        client.setConnectString("172.21.129.63:2181,172.21.129.63:2182");
        client.afterPropertiesSet();

        client.put("/conf/comm", "这是一个通用配置的主节点");

        Thread.sleep(5000l);

        client.put("/conf/app1/jdbc.url", "这是一个数据库连接串");
    }
}
