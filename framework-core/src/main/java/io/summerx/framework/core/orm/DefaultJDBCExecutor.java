package io.summerx.framework.core.orm;

import org.springframework.jdbc.core.*;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

/**
 * Created by xiayg on 6/27/2016.
 */
public class DefaultJDBCExecutor implements JDBCExecutor {

    private JdbcTemplate jdbcTemplate;

    @Override
    public <T> T executeQueryForUniqueObject(Class<T> returnType, String sql, Object[] params, T defaultValue) throws DAOException {
        SingleColumnRowMapper scrm;
        BeanPropertyRowMapper bprm;
        ColumnMapRowMapper cmrm;
        RowMapper rm = null;

        Object o1 = jdbcTemplate.queryForObject("", Object.class);   // SingleColumnRowMapper
        Object o2 = jdbcTemplate.queryForMap("");                    // ColumnMapRowMapper
        return null;
    }

    @Override
    public <T> T executeQueryForUniqueObject(Class<T> returnType, String sql, Object[] params) throws DAOException {
        return null;
    }

    @Override
    public <T> T executeQueryForUniqueObject(Class<T> returnType, String sql, T defaultValue) throws DAOException {
        return null;
    }

    @Override
    public <T> T executeQueryForUniqueObject(Class<T> returnType, String sql) throws DAOException {
        return null;
    }

    @Override
    public <T> T executeQueryForWrapBean(Class<T> returnType, String sql, Object[] params) throws DAOException {
        return null;
    }

    @Override
    public <T> T executeQueryForWrapBean(Class<T> returnType, String sql) throws DAOException {
        return null;
    }

    @Override
    public <T> List<T> queryForWrapBeanListBySql(Class<T> returnType, String sql, Object[] params, Object pagination) throws DAOException {
        return null;
    }

    @Override
    public <T> List<T> queryForWrapBeanListBySql(Class<T> returnType, String sql, Object[] params) throws DAOException {
        return null;
    }

    @Override
    public <T> List<T> queryForWrapBeanListBySql(Class<T> returnType, String sql) throws DAOException {
        return null;
    }

    @Override
    public List<Object> queryForListBySql(String sql, Object[] params) throws DAOException {
        return null;
    }

    @Override
    public List<Object> queryForListBySql(String sql) throws DAOException {
        return null;
    }

    @Override
    public <T> List<T> queryForListBySql(Class<T> returnType, String sql, Object[] params, RowMapper<T> rowMapper) throws DAOException {
        return null;
    }

    @Override
    public <T> List<T> queryForListBySql(Class<T> returnType, String sql, Object[] params, RowMapper<T> rowMapper, Object pagination) throws DAOException {
        return null;
    }

    @Override
    public int executeSql(String sql, Object[] params) throws DAOException {
        return 0;
    }

    @Override
    public int executeSql(String sql) throws DAOException {
        return 0;
    }

    @Override
    public int[] executeBatchSql(String sql, BatchPreparedStatementSetter pss) throws DAOException {
        return new int[0];
    }

    @Override
    public int[] executeBatchSql(String[] sqls) throws DAOException {
        return new int[0];
    }

    @Override
    public int[] executeBatchSql(Collection<String> sqls) throws DAOException {
        return new int[0];
    }

    @Override
    public <T> T executeProcedure(String callString, CallableStatementCallback<T> callableStatementCallback) throws DAOException {
        return null;
    }

    @Override
    public Date getDbCurrentTime() throws DAOException {
        return null;
    }
}
