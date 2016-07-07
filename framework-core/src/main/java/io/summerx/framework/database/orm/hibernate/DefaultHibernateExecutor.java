package io.summerx.framework.database.orm.hibernate;

import io.summerx.framework.core.pagination.OrderablePagination;
import io.summerx.framework.core.pagination.PageOrder;
import io.summerx.framework.core.pagination.Pagination;
import io.summerx.framework.database.SQLHelper;
import io.summerx.framework.database.orm.BaseEntity;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

public class DefaultHibernateExecutor implements HibernateExecutor {

    // Logger
    private Logger logger = LoggerFactory.getLogger(getClass());

    // Hibernate SessionFacatory
    private SessionFactory sessionFactory;

    public DefaultHibernateExecutor(SessionFactory sessionFactory) {
        Assert.notNull(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public <T> T get(Class<T> clazz, Serializable id) {
        return (T) getCurrentSession().get(clazz, id);
    }

    @Override
    public Serializable save(BaseEntity entity) {
        return getCurrentSession().save(entity);
    }
    @Override
    public Serializable[] save(BaseEntity[] entites) {
        if (entites == null || entites.length == 0) {
            return new Serializable[0];
        }

        Serializable[] result = new Serializable[entites.length];
        for (int i = 0; i < entites.length; i++) {
            result[i] = save(entites[i]);
        }
        return result;
    }
    @Override
    public Collection<Serializable> save(Collection<? extends BaseEntity> entites) {
        if (entites == null || entites.isEmpty()) {
            return Collections.emptyList();
        }

        List<Serializable> result = new ArrayList<>();
        for (BaseEntity entity : entites) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public void update(BaseEntity entity) {
        getCurrentSession().update(entity);
    }
    @Override
    public void update(BaseEntity[] entites) {
        if (entites == null || entites.length == 0) {
            return;
        }
        for (BaseEntity entity : entites) {
            update(entity);
        }
    }
    @Override
    public void update(Collection<? extends BaseEntity> entites) {
        if (entites == null || entites.isEmpty()) {
            return;
        }
        for (BaseEntity entity : entites) {
            update(entity);
        }
    }

    @Override
    public void saveOrUpdate(BaseEntity entity) {
        getCurrentSession().saveOrUpdate(entity);
    }
    @Override
    public void saveOrUpdate(BaseEntity[] entites) {
        if (entites == null || entites.length == 0) {
            return;
        }
        for (BaseEntity entity : entites) {
            saveOrUpdate(entity);
        }
    }
    @Override
    public void saveOrUpdate(Collection<? extends BaseEntity> entites) {
        if (entites == null || entites.isEmpty()) {
            return;
        }
        for (BaseEntity entity : entites) {
            saveOrUpdate(entity);
        }
    }

    @Override
    public void delete(BaseEntity entity) {
        getCurrentSession().delete(entity);
    }
    @Override
    public void delete(BaseEntity[] entites) {
        if (entites == null || entites.length == 0) {
            return;
        }
        for (BaseEntity entity : entites) {
            delete(entity);
        }
    }
    @Override
    public void delete(Collection<? extends BaseEntity> entites) {
        if (entites == null || entites.isEmpty()) {
            return;
        }
        for (BaseEntity entity : entites) {
            delete(entity);
        }
    }

    @Override
    public <T> List<T> executeQuery(DetachedCriteria detachedCriteria) {
        return executeQuery(detachedCriteria, null);
    }
    @Override
    public <T> List<T> executeQuery(DetachedCriteria detachedCriteria, OrderablePagination pagination) {
        CriteriaImpl criteria = (CriteriaImpl) detachedCriteria.getExecutableCriteria(getCurrentSession());
        // 分页排序信息
        if (pagination != null) {
            // 原来的投影
            Projection orginalProjection = criteria.getProjection();
            ResultTransformer orginalResultTransformer = criteria.getResultTransformer();
            Number count = (Number) criteria.setProjection(Projections.rowCount()).uniqueResult();
            if (count != null) {
                // 设置总记录数
                pagination.setTotalCount(count.intValue());
            }
            // 将原来的投影重新设置
            criteria.setProjection(orginalProjection);
            criteria.setResultTransformer(orginalResultTransformer);
            // 设置分页条件
            applyOrderablePagination(criteria, pagination);
        }

        return criteria.list();
    }
    @Override
    public <T> T uniqueResult(DetachedCriteria detachedCriteria) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
        return (T) criteria.uniqueResult();
    }

    @Override
    public <T> List<T> executeQuery(final String hql) {
        return executeQuery(hql, (Object[]) null);
    }
    @Override
    public <T> List<T> executeQuery(final String hql, Pagination pagination) {
        return executeQuery(hql, (Object[]) null);
    }
    @Override
    public <T> List<T> executeQuery(final String hql, final Object... params) {
        return executeQuery(hql, params, null);
    }
    @Override
    public <T> List<T> executeQuery(final String hql, final Object[] params, Pagination pagination) {
        Query query = getCurrentSession().createQuery(hql);
        applyParameters(query, params);
        if (pagination != null) {
            // 将HQL转换成SQL
            String sql = translateHql2Sql(hql, (SessionFactoryImplementor) sessionFactory);
            // 执行 select count(*)并设置总记录数
            Query countQuery = getCurrentSession().createQuery(SQLHelper.count(sql));
            applyParameters(countQuery, params);
            Number count = (Number) countQuery.uniqueResult();
            if (count != null) {
                // 设置总记录数
                pagination.setTotalCount(count.intValue());
            }
            // 设置分页条件
            applyOrderablePagination(query, pagination);
        }
        return query.list();
    }
    @Override
    public <T> List<T> executeQuery(final String hql, final Map<String, Object> params) {
        return executeQuery(hql, params, null);
    }
    @Override
    public <T> List<T> executeQuery(final String hql, final Map<String, Object> params, Pagination pagination) {
        Query query = getCurrentSession().createQuery(hql);
        applyMapParameters(query, params);
        if (pagination != null) {
            // 将HQL转换成SQL
            String sql = translateHql2Sql(hql, (SessionFactoryImplementor) sessionFactory);
            // 执行 select count(*)并设置总记录数
            Query countQuery = getCurrentSession().createQuery(SQLHelper.count(sql));
            applyMapParameters(countQuery, params);
            Number count = (Number) countQuery.uniqueResult();
            if (count != null) {
                // 设置总记录数
                pagination.setTotalCount(count.intValue());
            }
            // 设置分页条件
            applyOrderablePagination(query, pagination);
        }
        return query.list();
    }

    @Override
    public <T> T uniqueResult(final String hql) {
        return uniqueResult(hql, (Object[]) null);
    }
    @Override
    public <T> T uniqueResult(final String hql, final Object[] params) {
        Query query = getCurrentSession().createQuery(hql);
        applyParameters(query, params);
        return (T) query.uniqueResult();
    }
    @Override
    public <T> T uniqueResult(final String hql, final Map<String, Object> params) {
        Query query = getCurrentSession().createQuery(hql);
        applyMapParameters(query, params);
        return (T) query.uniqueResult();
    }

    @Override
    public int executeUpdate(final String hql) {
        return executeUpdate(hql, (Object[]) null);
    }
    @Override
    public int executeUpdate(final String hql, final Object[] params) {
        Query query = getCurrentSession().createQuery(hql);
        applyParameters(query, params);

        return query.executeUpdate();
    }
    @Override
    public int executeUpdate(final String hql, final Map<String, Object> params) {
        Query query = getCurrentSession().createQuery(hql);
        applyMapParameters(query, params);

        return query.executeUpdate();
    }

    // 向Query设置参数
    protected void applyParameters(Query query, Object[] params) {
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
    }

    @Override
    public Session getRawExecutor() {
        return getCurrentSession();
    }

    // 向Query设置参数
    protected void applyMapParameters(Query query, Map<String, Object> params) {
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    // FIXME 向Query设置分页条件，暂时不支持排序。
    protected void applyOrderablePagination(Query query, Pagination pagination) {
        // 设置开始记录号和记录数
        query.setFirstResult(pagination.getFirstIndex());
        query.setMaxResults(pagination.getPageSize());
        // 排序（不支持）
    }

    // 向Criteria设置分页条件
    protected void applyOrderablePagination(CriteriaImpl criteria, OrderablePagination pagination) {
        // 设置开始记录号和记录数
        criteria.setFirstResult(pagination.getFirstIndex());
        criteria.setMaxResults(pagination.getPageSize());
        // 排序
        if (pagination.getOrders() != null) {
            for (PageOrder pageOrder : pagination.getOrders()) {
                if (pageOrder.isDescending()) {
                    criteria.addOrder(Order.desc(pageOrder.getPropertyName()));
                } else {
                    criteria.addOrder(Order.asc(pageOrder.getPropertyName()));
                }
            }
        }
    }

    // 转化HQL为SQL
    protected String translateHql2Sql(String hql, SessionFactoryImplementor sfi) {
        QueryTranslatorImpl qt = new QueryTranslatorImpl(hql, hql, null, sfi);
        qt.compile(null, false);
        return qt.getSQLString();
    }

    private Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
}
