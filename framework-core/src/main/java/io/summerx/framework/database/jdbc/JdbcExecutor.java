package io.summerx.framework.database.jdbc;

import io.summerx.framework.core.pagination.Pagination;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface JdbcExecutor {

    <T> List<T> executeQuery(final Class<T> clazz, final String sql, final Object... params);
    <T> List<T> executeQuery(final RowMapper<T> rowMapper, final String sql, final Object... params);
    <T> List<T> executeQuery(final Class<T> clazz, final String sql, Pagination pagination);
    <T> List<T> executeQuery(final Class<T> clazz, final String sql, final Object[] params, Pagination pagination);
    <T> List<T> executeQuery(final RowMapper<T> rowMapper, final String sql, final Object[] params, Pagination pagination);

    <T> T uniqueResult(final Class<T> clazz, final String sql, final Object... params);
    <T> T uniqueResult(final RowMapper<T> rowMapper, final String sql, final Object... params);

    <T> T getSingleColumnValue(final Class<T> clazz, final String sql, final Object... params);

    int executeUpdate(final String sql, final Object... params);

    int[] batchUpdate(final String... sql);
    int[] batchUpdate(final String sql, List<Object[]> params);

    JdbcTemplate getRawExecutor();
}
