package io.summerx.framework.zookeeper.curator;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractPropertyResolver;
import org.springframework.util.ClassUtils;

import java.io.UnsupportedEncodingException;
import java.util.Set;

public class CuratorZooKeeperPropertyResolver extends AbstractPropertyResolver {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Set<PathChildrenCache> caches;

    @Override
    protected String getPropertyAsRawString(String key) {
        return getProperty(key, String.class, false);
    }

    @Override
    public boolean containsProperty(String key) {
        if (this.caches != null) {
            for (PathChildrenCache cache : this.caches) {
                if (cache.getCurrentData(key) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, String.class, true);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetValueType) {
        return getProperty(key, targetValueType, true);
    }

    @Override
    public <T> Class<T> getPropertyAsClass(String key, Class<T> targetValueType) {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("getPropertyAsClass(\"%s\", %s)", key, targetValueType.getSimpleName()));
        }
        if (this.caches != null) {
            for (PathChildrenCache cache : this.caches) {
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Searching for key '%s' in [%s]", key, "FIXME"));
                }
                String value = getPropertyValue(cache, key);
                if (value != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Found key '%s' in [%s] with value '%s'", key, "FIXME", value));
                    }
                    Class<?> clazz;
                    try {
                        clazz = ClassUtils.forName(value, null);
                    } catch (Exception ex) {
                        throw new IllegalArgumentException(String.format("Could not find/load class %s during attempt to convert to %s", value, targetValueType.getName()), ex);
                    }
                    if (!targetValueType.isAssignableFrom(clazz)) {
                        throw new IllegalArgumentException(String.format("Actual type %s is not assignable to expected type %s", value, targetValueType.getName()));
                    }
                    @SuppressWarnings("unchecked")
                    Class<T> targetClass = (Class<T>) clazz;
                    return targetClass;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Could not find key '%s' in any property source. Returning [null]", key));
        }
        return null;
    }

    protected <T> T getProperty(String key, Class<T> targetValueType, boolean resolveNestedPlaceholders) {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("getProperty(\"%s\", %s)", key, targetValueType.getSimpleName()));
        }

        if (this.caches != null) {
            for (PathChildrenCache cache : this.caches) {
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Searching for key '%s' in [%s]", key, "FIXME"));
                }

                byte[] bytes = cache.getCurrentData(key).getData();
                if (bytes != null) {
                    String value;
                    try {
                        value = new String(bytes, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new IllegalArgumentException("");
                    }
                    Class<?> valueType = value.getClass();
                    if (resolveNestedPlaceholders) {
                        value = resolveNestedPlaceholders(value);
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Found key '%s' in [%s] with type [%s] and value '%s'",
                                key, "FIXME", valueType.getSimpleName(), value));
                    }
                    if (!this.conversionService.canConvert(valueType, targetValueType)) {
                        throw new IllegalArgumentException(String.format(
                                "Cannot convert value [%s] from source type [%s] to target type [%s]",
                                value, valueType.getSimpleName(), targetValueType.getSimpleName()));
                    }
                    return this.conversionService.convert(value, targetValueType);
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Could not find key '%s' in any property source. Returning [null]", key));
        }
        return null;
    }

    private String getPropertyValue(PathChildrenCache cache, String key) {
        ChildData childData = cache.getCurrentData(key);
        if (childData == null) {
            return null;
        }
        byte[] bytes = childData.getData();
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("");
        }
    }
}
