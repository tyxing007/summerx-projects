import io.summerx.framework.zookeeper.curator.CuratorZooKeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiayg on 6/29/2016.
 */
public class CuratorZooKeeperTest {

    public static void main(String[] args) throws Exception {

        final CuratorZooKeeperClient client = new CuratorZooKeeperClient();
        client.setConnectString("172.21.129.63:2181,172.21.129.63:2182");
        client.afterPropertiesSet();

        client.getRawClient().getConnectionStateListenable().addListener(new ConnectionStateListener() {
            public void stateChanged(CuratorFramework client, ConnectionState state) {
                System.out.println(String.format("Connection %s", state.name()));
            }
        });

        client.getRawClient().checkExists().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(String.format("%s %s", event.getPath(), event.getType().name()));

                try {
                    System.out.println(event.getPath() + "的值 = " + client.get("/conf/comm"));
                    client.getRawClient().checkExists().usingWatcher(this).forPath(event.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).forPath("/conf/comm");


        PathChildrenCache cache = new PathChildrenCache(client.getRawClient(), "/conf/comm", true);
        // 添加监听
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("===================================================================================");
                        System.out.println("Node added: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        System.out.println("===================================================================================");
                        break;
                    case CHILD_UPDATED:
                        System.out.println("===================================================================================");
                        System.out.println("Node updated: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        System.out.println("===================================================================================");
                        break;
                    case CHILD_REMOVED:
                        System.out.println("===================================================================================");
                        System.out.println("Node removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        System.out.println("===================================================================================");
                        break;
                    case CONNECTION_RECONNECTED:
                        System.out.println("===================================================================================");
                        System.out.println("Connection reconnected");
                        System.out.println("===================================================================================");
                        cache.rebuild();
                        break;
                    default:
                        break;
                }
            }
        });
        // 启动
        cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

//        byte[] bytes = cache.getCurrentData("/conf/comm/jdbc.url").getData();
//        if (bytes != null) {
//            System.out.println("/conf/comm/jdbc.url的值（C) = " + new String(bytes, "UTF-8"));
//        } else {
//            System.out.println("/conf/comm/jdbc.url的值（C) = null");
//        }

//        System.out.println("/conf/comm的值 = " + client.get("/conf/comm"));
//        System.out.println("/conf/comm/jdbc.url的值 = " + client.get("/conf/comm/jdbc.url"));

        Thread.sleep(Integer.MAX_VALUE);
    }
}
