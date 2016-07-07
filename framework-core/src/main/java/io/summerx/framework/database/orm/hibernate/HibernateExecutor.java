package io.summerx.framework.database.orm.hibernate;

import io.summerx.framework.core.pagination.OrderablePagination;
import io.summerx.framework.core.pagination.Pagination;
import io.summerx.framework.database.orm.BaseEntity;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.*;

public interface HibernateExecutor {

    <T> T get(Class<T> clazz, Serializable id);

    Serializable save(BaseEntity entity);
    Serializable[] save(BaseEntity[] entites);
    Collection<Serializable> save(Collection<? extends BaseEntity> entites);

    void update(BaseEntity entity);
    void update(BaseEntity[] entites);
    void update(Collection<? extends BaseEntity> entites);

    void saveOrUpdate(BaseEntity entity);
    void saveOrUpdate(BaseEntity[] entites);
    void saveOrUpdate(Collection<? extends BaseEntity> entites);

    void delete(BaseEntity entity);
    void delete(BaseEntity[] entites);
    void delete(Collection<? extends BaseEntity> entites);

    <T> List<T> executeQuery(DetachedCriteria detachedCriteria);
    <T> List<T> executeQuery(DetachedCriteria detachedCriteria, OrderablePagination pagination);
    <T> T uniqueResult(DetachedCriteria detachedCriteria);

    <T> List<T> executeQuery(final String hql);
    <T> List<T> executeQuery(final String hql, Pagination pagination);
    <T> List<T> executeQuery(final String hql, final Object[] params);
    <T> List<T> executeQuery(final String hql, final Object[] params, Pagination pagination);
    <T> List<T> executeQuery(final String hql, final Map<String, Object> params);
    <T> List<T> executeQuery(final String hql, final Map<String, Object> params, Pagination pagination);

    <T> T uniqueResult(final String hql);
    <T> T uniqueResult(final String hql, final Object[] params);
    <T> T uniqueResult(final String hql, final Map<String, Object> params);

    int executeUpdate(final String hql);
    int executeUpdate(final String hql, final Object[] params);
    int executeUpdate(final String hql, final Map<String, Object> params);

    Session getRawExecutor();
}
