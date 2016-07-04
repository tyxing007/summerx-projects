package io.summerx.framework.cache.memcached;

import com.danga.MemCached.MemCachedClient;
import io.summerx.framework.cache.CustomizedCache;
import net.sf.ehcache.Element;
import net.spy.memcached.MemcachedClient;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.concurrent.Callable;

/**
 * 想要更多功能请使用getNativeCache
 */
public class MemcachedCustomizedCache implements Cache {

    private final String name;

    // private final MemCachedClient client;

    MemcachedClient client;

    private int expired = 1000;

    public MemcachedCustomizedCache(String name, MemcachedClient client) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(client, "Memcached must not be null");
        this.name = name;
        this.client = client;
    }

    private Object lookup(Object key) {
        if (!(key instanceof String)) {
            return null;
        }

        return this.client.get((String) key);
    }

    private ValueWrapper toValueWrapper(Object value) {
        return value != null ? new SimpleValueWrapper(value) : null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return client;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value = lookup(key);
        return toValueWrapper(value);
    }

    @Override
    public <T> T get(Object key, Class<T> clazz) {
        Object value = lookup(key);
        return value != null ? clazz.cast(value) : null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        client.set(key.toString(), expired, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper vw = get(key);
        if (vw != null) {
            return vw;
        }
        put(key, value);
        return toValueWrapper(value);
    }

    @Override
    public void evict(Object key) {
        client.delete(key.toString());
    }

    @Override
    public void clear() {
        client.flush();
    }
}
