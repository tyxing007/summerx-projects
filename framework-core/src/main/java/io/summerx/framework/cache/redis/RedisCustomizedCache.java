package io.summerx.framework.cache.redis;

import io.summerx.framework.cache.CustomizedCache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by xiayg on 7/1/2016.
 */
public class RedisCustomizedCache extends RedisCache implements CustomizedCache {

    private final byte[] keyPrefix;

    public RedisCustomizedCache(String name, byte[] prefix, RedisTemplate<? extends Object, ? extends Object> template,
                      long expiration) {
        super(name, prefix, template, expiration);
        keyPrefix = prefix;
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
    public void put(Object key, Object value, int expired) {
        RedisOperations nativeCache = (RedisOperations) getNativeCache();
        put(new RedisCacheElement(new RedisCacheKey(key).usePrefix(keyPrefix).withKeySerializer(
                nativeCache.getKeySerializer()), value).expireAfter(expired));
    }

    @Override
    public void putIfAbsent(Object key, Object value, int expired) {
        RedisOperations nativeCache = (RedisOperations) getNativeCache();
        putIfAbsent(new RedisCacheElement(new RedisCacheKey(key).usePrefix(keyPrefix).withKeySerializer(
                nativeCache.getKeySerializer()), value).expireAfter(expired));
    }

    @Override
    public Set<Object> keySet() {
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) == null ? false : true;
    }

    @Override
    public int size() {
        return 0;
    }
}
