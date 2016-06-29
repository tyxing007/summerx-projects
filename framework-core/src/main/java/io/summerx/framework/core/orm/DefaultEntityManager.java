package io.summerx.framework.core.orm;

import org.hibernate.SessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

import javax.sql.DataSource;

public class DefaultEntityManager {

    DataSource dataSource;

    JdbcTemplate jdbcTemplate;

    SqlSessionTemplate sqlSessionTemplate;

    HibernateTemplate hibernateTemplate;

    public DefaultEntityManager(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(this.dataSource);
        sqlSessionTemplate = new SqlSessionTemplate(null);
        hibernateTemplate = new HibernateTemplate(null);

        SessionFactory sf = null;

    }


}
