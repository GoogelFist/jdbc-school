package ua.com.foxminded.school.dao;

import java.sql.Connection;

public interface Connector {
    Connection getConnection();
}
