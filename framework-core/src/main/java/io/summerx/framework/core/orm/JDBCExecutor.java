package io.summerx.framework.core.orm;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;
import java.util.List;

/**
 * 原生SQL操作接口
 */
public interface JDBCExecutor {

    /**
     * 根据给定的SQL查询，返回给定requiredType的实例。
     * 对于需要包装的Bean的请使用<T> T queryForWrapBeanBySql(Class<T> requiredType, String sql, Object[] params)方法
     * 此方法只针对普通的单个对象（不需要包装），如返回值为Integer、Long、Ojbect[]等等。
     * @param <T>
     * @param returnType
     * @param sql
     * @param params
     * @param defaultValue 如果查询结果为null，则返回defaultValue
     * @return
     */
    <T> T executeQueryForUniqueObject(Class<T> returnType, String sql, Object[] params, T defaultValue) throws DAOException;
    <T> T executeQueryForUniqueObject(Class<T> returnType, String sql, Object[] params) throws DAOException;
    <T> T executeQueryForUniqueObject(Class<T> returnType, String sql, T defaultValue) throws DAOException;
    <T> T executeQueryForUniqueObject(Class<T> returnType, String sql) throws DAOException;

    /**
     * 根据给定的SQL查询，返回给定returnType类型的Bean。
     * SQL里select字段的别名必须与Bean对应的属性名相同，并且表字段的类型也必须与Bean对应的属性的类型一致。
     * @param <T>
     * @param returnType
     * @param sql
     * @param params
     * @return
     */
    <T> T executeQueryForWrapBean(Class<T> returnType, String sql, Object[] params) throws DAOException;
    <T> T executeQueryForWrapBean(Class<T> returnType, String sql) throws DAOException;

    /**
     * 根据给定的SQL查询，返回给定returnType类型的Bean List。
     * SQL里select字段的别名必须与Bean对应的属性名相同，并且表字段的类型也必须与Bean对应的属性的类型一致。
     * @param <T>
     * @param returnType
     * @param sql
     * @param params
     * @param pagination
     * @return
     */
    <T> List<T> queryForWrapBeanListBySql(Class<T> returnType, String sql, Object[] params, Object pagination) throws DAOException;
    <T> List<T> queryForWrapBeanListBySql(Class<T> returnType, String sql, Object[] params) throws DAOException;
    <T> List<T> queryForWrapBeanListBySql(Class<T> returnType, String sql) throws DAOException;

    /**
     * 根据给定的sql查询结果
     * 默认的RowMapper为ObjectArrayRowMapper
     * @param sql
     * @param params
     * @return
     * @throws DAOException
     */
    List<Object> queryForListBySql(String sql, Object[] params) throws DAOException;
    List<Object> queryForListBySql(String sql) throws DAOException;

    <T> List<T> queryForListBySql(Class<T> returnType, String sql, Object[] params, RowMapper<T> rowMapper) throws DAOException;
    <T> List<T> queryForListBySql(Class<T> returnType, String sql, Object[] params, RowMapper<T> rowMapper, Object pagination) throws DAOException;

    /**
     * 执行SQL，返回SQL执行后影响的记录条数
     * sql可以是任意的SQL，包括“INSERT, UPDATE, DELETE, SELECT...”
     * @param sql
     * @param params
     * @return
     */
    int executeSql(String sql, Object[] params) throws DAOException;

    /**
     * 执行SQL，返回SQL执行后影响的记录条数
     * sql可以是任意的SQL，包括“INSERT, UPDATE, DELETE, SELECT...”
     * @param sql
     * @return
     */
    int executeSql(String sql) throws DAOException;

    /**
     * 对于同一种sql，不同参数进行批量操作。
     * 如有SQL："update t_actor set first_name = ?, last_name = ? where id = ?"
     * 传入参数actors，可以：
     * new BatchPreparedStatementSetter() {
     public void setValues(PreparedStatement ps, int i) throws SQLException {
     ps.setString(1, ((Actor)actors.get(i)).getFirstName());
     ps.setString(2, ((Actor)actors.get(i)).getLastName());
     ps.setLong(3, ((Actor)actors.get(i)).getId().longValue());
     }
     * @param sql
     * @param pss
     * @return
     */
    int[] executeBatchSql(String sql, BatchPreparedStatementSetter pss) throws DAOException;

    /**
     * 批量执行SQL(jdbc的batch)，返回批量SQL执行后影响的记录条数
     * sql可以是任意的SQL，包括“INSERT, UPDATE, DELETE, SELECT...”
     * @param sqls
     * @return
     */
    int[] executeBatchSql(String[] sqls) throws DAOException;

    /**
     * 批量执行SQL，返回批量SQL执行后影响的记录条数
     * sql可以是任意的SQL，包括“INSERT, UPDATE, DELETE, SELECT...”
     * @param sqls
     * @return
     */
    int[] executeBatchSql(Collection<String> sqls) throws DAOException;

    /**
     * 执行存储过程callString
     * @param callString
     * @param callableStatementCallback
     * @see org.springframework.jdbc.core.CallableStatementCallback
     * @return
     */
    <T> T executeProcedure(String callString, CallableStatementCallback<T> callableStatementCallback) throws DAOException;

    java.sql.Date getDbCurrentTime() throws DAOException;
}
