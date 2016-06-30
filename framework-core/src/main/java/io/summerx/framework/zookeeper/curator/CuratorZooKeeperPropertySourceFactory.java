package io.summerx.framework.zookeeper.curator;

import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class CuratorZooKeeperPropertySourceFactory implements FactoryBean<CuratorZooKeeperPropertySource>, InitializingBean {

    /**
     * ZooKeeperClient
     */
    private CuratorZooKeeperClient client;

    /**
     * path
     */
    private String path;

    /**
     * PropertySource
     */
    private CuratorZooKeeperPropertySource source;

    /**
     * watcher
     */
    private PathChildrenCacheListener watcher;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建PathChildrenCache
        PathChildrenCache cache = new PathChildrenCache(client.getRawClient(), path, true);
        // 添加监听
        if (watcher != null) {
            cache.getListenable().addListener(watcher);
        }
        // 启动
        cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

        // 创建PropertySource
        source = new CuratorZooKeeperPropertySource(path, cache);
        // 设置编码
        source.setCharset(client.getCharset());
    }

    @Override
    public CuratorZooKeeperPropertySource getObject() throws Exception {
        return source;
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorZooKeeperPropertySource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setClient(CuratorZooKeeperClient client) {
        this.client = client;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSource(CuratorZooKeeperPropertySource source) {
        this.source = source;
    }

    public void setWatcher(PathChildrenCacheListener watcher) {
        this.watcher = watcher;
    }
}
