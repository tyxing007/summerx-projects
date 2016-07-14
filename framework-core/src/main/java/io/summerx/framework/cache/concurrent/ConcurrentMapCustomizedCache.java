package io.summerx.framework.cache.concurrent;

import io.summerx.framework.cache.CustomizedCache;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentMapCustomizedCache extends ConcurrentMapCache implements CustomizedCache {

    public ConcurrentMapCustomizedCache(String name, boolean allowNullValues) {
        super(name, allowNullValues);
    }

    public ConcurrentMapCustomizedCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues) {
        super(name, store, allowNullValues);
    }

    public ConcurrentMapCustomizedCache(String name) {
        super(name);
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
        put(key, value);
    }

    @Override
    public void putIfAbsent(Object key, Object value, int expired) {
        putIfAbsent(key, value);
    }

    @Override
    public Set<Object> keySet() {
        return getNativeCache().keySet();
    }

    @Override
    public boolean containsKey(Object key) {
        return getNativeCache().containsKey(key);
    }

    @Override
    public int size() {
        return getNativeCache().size();
    }
}
