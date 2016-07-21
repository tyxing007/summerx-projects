package io.summerx.framework.database.jdbc;

import io.summerx.framework.core.pagination.Pagination;
import io.summerx.framework.database.SQLHelper;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import javax.sql.DataSource;
import java.util.List;

public class DefaultJdbcExecutor implements JdbcExecutor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private JdbcTemplate jdbcTemplate;

    private Dialect dialect;

    public DefaultJdbcExecutor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        // FIXME
        dialect = new Oracle10gDialect();
    }

    @Override
    public <T> List<T> executeQuery(final Class<T> clazz, final String sql, final Object... params) {
        return executeQuery(clazz, sql, params, null);
    }
    public <T> List<T> executeQuery(final RowMapper<T> rowMapper, final String sql, final Object... params) {
        return executeQuery(rowMapper, sql, params, null);
    }
    @Override
    public <T> List<T> executeQuery(final Class<T> clazz, final String sql, Pagination pagination) {
        return executeQuery(clazz, sql, null, pagination);
    }
    @Override
    public <T> List<T> executeQuery(final Class<T> clazz, final String sql, final Object[] params, Pagination pagination) {
        return executeQuery(new BeanPropertyRowMapper<>(clazz), sql, params, pagination);
    }
    @Override
    public <T> List<T> executeQuery(final RowMapper<T> rowMapper, final String sql, final Object[] params, Pagination pagination) {
        if (pagination == null) {
            return jdbcTemplate.query(sql, params, rowMapper);
        }

        // 总记录数
        Number count = uniqueResult(Number.class, SQLHelper.count(sql), params);
        if (count != null) {
            pagination.setTotalCount(count.intValue());
        }
        // 分页查询
        String sqlToUse = dialect.getLimitString(sql, pagination.getFirstIndex(), pagination.getPageSize());
//        RowSelection rs = new RowSelection();
//        rs.setFirstRow(pagination.getFirstIndex());
//        rs.setFetchSize(pagination.getPageSize());
//        String sqlToUse = dialect.buildLimitHandler(sql, rs).getProcessedSql();
        Object[] paginationParam = {pagination.getFirstIndex(), pagination.getFirstIndex() + pagination.getPageSize()};

        return this.jdbcTemplate.query(sqlToUse, appendParams(params, paginationParam), rowMapper);
    }

    @Override
    public <T> T uniqueResult(final Class<T> clazz, final String sql, final Object... params) {
        List<T> results = executeQuery(clazz, sql, params);
        if (results == null || results.size() == 0) {
            return null;
        }
        if (results != null && results.size() > 1) {
            // Error?
            logger.warn("Incorrect result size: expected 1, actual " + results.size());
        }
        return results.get(0);
    }
    @Override
    public <T> T uniqueResult(final RowMapper<T> rowMapper, final String sql, final Object... params) {
        List<T> results = executeQuery(rowMapper, sql, params);
        if (results == null || results.size() == 0) {
            return null;
        }
        if (results != null && results.size() > 1) {
            // Error?
            logger.warn("Incorrect result size: expected 1, actual " + results.size());
        }
        return results.get(0);
    }

    @Override
    public <T> T getSingleColumnValue(final Class<T> clazz, final String sql, final Object... params) {
        return uniqueResult(new SingleColumnRowMapper<>(clazz), sql, params);
    }

    @Override
    public int executeUpdate(final String sql, final Object... params) {
        return this.jdbcTemplate.update(sql, params);
    }

    @Override
    public int[] batchUpdate(final String... sql) {
        return this.jdbcTemplate.batchUpdate(sql);
    }
    @Override
    public int[] batchUpdate(final String sql, List<Object[]> params) {
        return this.jdbcTemplate.batchUpdate(sql, params);
    }

    @Override
    public JdbcTemplate getRawExecutor() {
        return this.jdbcTemplate;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    protected Object[] appendParams(Object[] param1, Object[] param2) {
        if (param1 == null) {
            return param2;
        }
        if (param2 == null) {
            return param1;
        }

        int len1 = param1.length;
        int len2 = param2.length;
        Object[] params = new Object[len1 + len2];
        for (int i = 0; i < len1; i++) {
            params[i] = param1[i];
        }
        for (int i = 0; i < len2; i++) {
            params[len1 + i] = param2[i];
        }
        return params;
    }
}
