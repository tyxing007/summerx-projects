package io.summerx.framework.cache;

/**
 * Created by xiayg on 7/1/2016.
 */
public abstract class AbstractCustomizedCache implements CustomizedCache {

    @Override
    public Object getValue(Object key) {
        ValueWrapper vw = get(key);
        return vw == null ? null : vw.get();
    }

    @Override
    public <T> T getValue(Object key, Class<T> clazz) {
        return get(key, clazz);
    }
}
