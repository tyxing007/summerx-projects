package io.summerx.sso.authentication.service;

import io.summerx.framework.database.jdbc.JdbcExecutor;
import io.summerx.sso.authentication.userdetails.DefaultUserDetails;
import io.summerx.sso.authentication.userdetails.UserDetails;
import org.springframework.jdbc.core.RowMapper;

/**
 * <p>
 * 请在这里添加类说明.
 * </p>
 *
 * @author summerx
 * @Date 2016-07-19 4:49 PM
 */
public class DefaultUserService implements UserService {

    private static final String SQL = "select username, password, locked, expired from VW_SSO_USER where username=?";

    private JdbcExecutor jdbcExecutor;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return jdbcExecutor.uniqueResult((RowMapper<UserDetails>) (rs, rowNum) -> new DefaultUserDetails(rs.getString(1), rs.getString(2), rs.getBoolean(3), rs.getBoolean(4)),
                SQL, username);

    }

    public void setJdbcExecutor(JdbcExecutor jdbcExecutor) {
        this.jdbcExecutor = jdbcExecutor;
    }
}
