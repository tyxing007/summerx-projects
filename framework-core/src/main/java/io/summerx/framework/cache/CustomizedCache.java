package io.summerx.framework.cache;

import org.springframework.cache.Cache;

import java.util.Set;

/**
 * 扩展了一些方法
 */
public interface CustomizedCache extends Cache {

    Object getValue(Object key);

    <T> T getValue(Object key, Class<T> clazz);

    void put(Object key, Object value, long expired);

    void putIfAbsent(Object key, Object value, long expired);

    Set<Object> keySet();

    boolean containsKey(Object key);

    int size();
}
