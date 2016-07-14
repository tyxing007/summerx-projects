package io.summerx.framework.cache.ehcache;

import io.summerx.framework.cache.CustomizedCache;
import net.sf.ehcache.Ehcache;
import org.springframework.cache.ehcache.EhCacheCache;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EhCacheCustomizedCache extends EhCacheCache implements CustomizedCache {

    public EhCacheCustomizedCache(Ehcache ehcache) {
        super(ehcache);
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
        final List<Object> keys = getNativeCache().getKeys();
        return keys == null ? Collections.emptySet() : new HashSet<>(keys);
    }

    @Override
    public boolean containsKey(Object key) {
        return getNativeCache().isKeyInCache(key);
    }

    @Override
    public int size() {
        return getNativeCache().getSize();
    }
}
