package io.summerx.framework.zookeeper.curator;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.utils.ZKPaths;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CuratorZooKeeperPropertySource extends EnumerablePropertySource<PathChildrenCache> {

    /**
     * 编码
     */
    private String charset = "UTF-8";

    public CuratorZooKeeperPropertySource(String name, PathChildrenCache source) {
        super(name, source);
    }

    /**
     * 属性名称不包含节点路径，比如/conf/comm/jdbc.url对应的属性名是jdbc.url
     * @return
     */
    @Override
    public String[] getPropertyNames() {
        List<ChildData> children = this.source.getCurrentData();
        if (children != null) {
            List<String> names = children.stream().map((c) -> ZKPaths.getNodeFromPath(c.getPath())).collect(Collectors.toList());
            return names.toArray(new String[names.size()]);
        }

        return null;
    }

    /**
     * 获取属性时，只需要传节点名即可，比如获取/conf/comm/jdbc.url时，只需要传jdbc.url
     * @param name
     * @return
     */
    @Override
    public String getProperty(String name) {
        String realName = ZKPaths.makePath(this.getName(), name);

        ChildData childData = this.source.getCurrentData(realName);
        if (childData == null) {
            return null;
        }
        byte[] bytes = childData.getData();
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("unsupported encoding.", e);
        }
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
