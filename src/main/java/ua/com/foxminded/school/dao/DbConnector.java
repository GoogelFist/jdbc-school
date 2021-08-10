package ua.com.foxminded.school.dao;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DbConnector implements Connector {
    private static final String URL_KEY = "url";
    private static final String USER_KEY = "user";
    private static final String PASSWORD_KEY = "password";

    private final BasicDataSource dataSource = new BasicDataSource();

    public DbConnector(String propertyFileName) {
        ResourceBundle resBundle = ResourceBundle.getBundle(propertyFileName);
        String url = resBundle.getString(URL_KEY);
        String user = resBundle.getString(USER_KEY);
        String password = resBundle.getString(PASSWORD_KEY);

        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DaoException("Connection is failed", e);
        }
    }
}