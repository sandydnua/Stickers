package stickers.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import stickers.database.entity.Accaunt;
import stickers.database.entity.Boards;
import stickers.database.entity.Stickers;
import stickers.database.repositoryes.*;
import stickers.services.DbServices;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MySQLDatabaseManager implements DatabaseManager {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate template;


    @Override
    public SessionFactory getSessionFactory() {

        return sessionFactory;
    }

    public  List<String> executeQuery(String query) {
        return template.query(query, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int numRow) throws SQLException {
                /*for(int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {

                }*/
                System.out.println("----");
                return resultSet.getString("name");
            }
        });
    }


    @Override
    public  void updateQuery(String query) {
        // TODO надо обрабатывать ошибки
        template.update(query);
    }

    public  Session getSession() {
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        return session;
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        template = new JdbcTemplate(dataSource);
    }

    @Override
    public void close() {
        //TODO
        System.out.println("CLOSE");
        try {
            dataSource.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnect() {
        try {
            return (dataSource != null && !dataSource.getConnection().isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
