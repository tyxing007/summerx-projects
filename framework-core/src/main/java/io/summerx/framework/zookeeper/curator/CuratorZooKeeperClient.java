package io.summerx.framework.zookeeper.curator;

import java.util.List;

import io.summerx.framework.zookeeper.ZooKeeperClient;
import io.summerx.framework.zookeeper.ZookeeperProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * 封装了一些基本的操作，如果需要更高级的功能，请使用getRawClient
 *
 */
public class CuratorZooKeeperClient extends ZookeeperProperties implements ZooKeeperClient, InitializingBean {

	private CuratorFramework client;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 创建ZooKeeper客户端
		client = CuratorFrameworkFactory.builder().connectString(getConnectString())
				.connectionTimeoutMs(getConnectionTimeoutMs())
				.sessionTimeoutMs(getSessionTimeoutMs())
				.retryPolicy(new ExponentialBackoffRetry(getBaseSleepMs(), getMaxRetries(), getMaxSleepMs()))
				.build();

		// start
		client.start();

		// 等待n秒或连接可用
		client.blockUntilConnected(getBlockUntilConnectedWait(), getBlockUntilConnectedUnit());

		// 监听连接状态变化
		client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
			public void stateChanged(CuratorFramework client, ConnectionState state) {
				if (state == ConnectionState.LOST) {
					CuratorZooKeeperClient.this.stateChanged(0);
				} else if (state == ConnectionState.CONNECTED) {
					CuratorZooKeeperClient.this.stateChanged(1);
				} else if (state == ConnectionState.RECONNECTED) {
					CuratorZooKeeperClient.this.stateChanged(2);
				}
			}
		});
    }

	/**
	 * 返回原始的CuratorFramework，你可以直接操作CuratorFramework以获得更多功能
     */
	public CuratorFramework getRawClient() {
		return client;
	}

	public void put(String path, String value) throws Exception {
		put(path, value, false);
	}

	@Override
	public void put(String path, String value, boolean ephemeral) throws Exception {
		put(path, value, ephemeral, false);
	}

	public void put(String path, String value, boolean ephemeral, boolean sequential) throws Exception {
		if (value == null) {
			// Remove ? using empty ?
			value = "";
		}

		// 如果节点不存在，先创建节点
		Stat stat = client.checkExists().forPath(path);
		if (stat == null) {
			client.create()
					.creatingParentsIfNeeded()
					.withMode(ensureCreateMode(ephemeral, sequential))
					.forPath(path);
		}
		// 对节点赋值
		client.setData().forPath(path, value.getBytes(getCharset()));
	}

	@Override
	public String get(String path) throws Exception {
		byte[] bytes = client.getData().forPath(path);
		if (bytes == null) {
			return null;
		}
		return new String(bytes, getCharset());
	}
	
	@Override
	public void remove(String path) throws Exception {
		// 如果节点存在，则删除节点
		Stat stat = client.checkExists().forPath(path);
		if (stat != null) {
			client.delete().deletingChildrenIfNeeded().forPath(path);
		}
	}

	@Override
	public List<String> getChildren(String path) throws Exception {
		return client.getChildren().forPath(path);
	}

	/**
	 * ZooKeeper状态变化
	 * @param state
	 */
	protected void stateChanged(int state) {
	}

    private static CreateMode ensureCreateMode(boolean ephemeral, boolean sequential) {
        if (!ephemeral && !sequential) {
            return CreateMode.PERSISTENT;
        } else if (!ephemeral && sequential) {
            return CreateMode.PERSISTENT_SEQUENTIAL;
        } else if (ephemeral && !sequential) {
            return CreateMode.EPHEMERAL;
        } else {
            return CreateMode.EPHEMERAL_SEQUENTIAL;
        }
    }
}
