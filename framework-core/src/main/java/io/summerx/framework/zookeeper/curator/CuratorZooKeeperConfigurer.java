package io.summerx.framework.zookeeper.curator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 属性名称相同时，ZooKeeper上的配置会覆盖资源文件，ZooKeeper上多个zkpath下有相同属性时，后面的会覆盖前面
 * 像下面的配置将按/conf/app1 -> /conf/comm -> 资源文件属性获取配置
 * <bean class="io.summerx.framework.zookeeper.curator.CuratorZooKeeperConfigurer">
 * 		<property name="client" ref="zkcilent"/>
 * 		<property name="zkpaths">
 * 		 	<list>
 * 		 	  <value>/conf/comm</value>
 * 		 	  <value>/conf/app1</value>
 * 		 	</list>
 * 		</property>
 * 		<property name="locations">
 * 		 	<list>
 * 		 	  <value>classpath:jdbc.properties</value>
 * 		 	  <value>classpath:mail.properties</value>
 * 		 	</list>
 * 		</property>
 * </bean>
 */
public class CuratorZooKeeperConfigurer extends PropertyPlaceholderConfigurer implements InitializingBean {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * ZooKeeper客户端
	 */
	private CuratorZooKeeperClient client;

	/**
	 * ZK路径
	 */
	private List<String> zkpaths;

	/**
	 * ZooKeeper的KV属性源
	 */
	private Set<CuratorZooKeeperPropertySource> sources;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (client != null && zkpaths != null) {
			sources = new LinkedHashSet<>();
			for (String path : zkpaths) {
				// 创建PathChildrenCache
				PathChildrenCache cache = new PathChildrenCache(client.getRawClient(), path, true);
				// 添加监听
				cache.getListenable().addListener(new PathChildrenCacheListener() {
					@Override
					public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
						switch (event.getType()) {
							case CHILD_ADDED:
								logger.info("===================================================================================");
								logger.info("Node added: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
								logger.info("===================================================================================");
								dataChanged(ZKPaths.getNodeFromPath(event.getData().getPath()));
								break;
							case CHILD_UPDATED:
								logger.info("===================================================================================");
								logger.info("Node updated: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
								logger.info("===================================================================================");
								dataChanged(ZKPaths.getNodeFromPath(event.getData().getPath()));
								break;
							case CHILD_REMOVED:
								logger.info("===================================================================================");
								logger.info("Node removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
								logger.info("===================================================================================");
								dataChanged(ZKPaths.getNodeFromPath(event.getData().getPath()));
								break;
							case CONNECTION_RECONNECTED:
								logger.info("===================================================================================");
								logger.info("Connection reconnected");
								logger.info("===================================================================================");
								break;
							default:
								break;
						}
					}
				});
				// 启动
				cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

				// 创建PropertySource
				CuratorZooKeeperPropertySource source = new CuratorZooKeeperPropertySource(path, cache);
				sources.add(source);
				// 设置编码
				source.setCharset(client.getCharset());
			}
		}
	}

	/**
	 * Return a merged Properties instance containing both the
	 * loaded properties and properties set on this FactoryBean.
	 */
	@Override
	protected Properties mergeProperties() throws IOException {
		Properties mergeProperties = super.mergeProperties();

		if (mergeProperties == null) {
			mergeProperties = new Properties();
		}

		if (sources == null) {
			return mergeProperties;
		}

		for (CuratorZooKeeperPropertySource source : sources) {
			String[] names = source.getPropertyNames();
			if (names != null) {
				for (String name : names) {
					String value = source.getProperty(name);
					if (value != null) {
						mergeProperties.put(name, source.getProperty(name));
					}
				}
			}
		}

		return mergeProperties;
	}

	protected String resolveZKProperty(String name) {
		if (sources != null) {
			for (CuratorZooKeeperPropertySource source : sources) {
				String value = source.getProperty(name);
				if (value != null) {
					return value;
				}
			}
		}

		return null;
	}

	protected void dataChanged(String name) {
	}

	public void setClient(CuratorZooKeeperClient client) {
		this.client = client;
	}

	public void setZkpaths(List<String> zkpaths) {
		this.zkpaths = zkpaths;
	}
}
