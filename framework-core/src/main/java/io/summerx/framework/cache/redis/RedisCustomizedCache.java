package io.summerx.framework.cache.redis;

import io.summerx.framework.cache.CustomizedCache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by xiayg on 7/1/2016.
 */
public class RedisCustomizedCache extends RedisCache implements CustomizedCache {

    public RedisCustomizedCache(String name, byte[] prefix, RedisTemplate<? extends Object, ? extends Object> template,
                      long expiration) {
        super(name, prefix, template, expiration);
    }


    @Override
    public Object getValue(Object key) {
        ValueWrapper vw = get(key);
        return vw == null ? null : vw.get();
    }

    @Override
    public <T> T getValue(Object key, Class<T> clazz) {
        return get(key, clazz);
    }

    @Override
    public void put(Object key, Object value, long expired) {
        put(key, value);
    }

    @Override
    public void putIfAbsent(Object key, Object value, long expired) {
        putIfAbsent(key, value);
    }

    @Override
    public Set<Object> keySet() {
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }
}
