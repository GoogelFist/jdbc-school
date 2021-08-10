package ua.com.foxminded.school.dao;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.MissingResourceException;

import static org.junit.jupiter.api.Assertions.*;

class DbConnectorTest {

    @Test
    void connectionShouldBe() {
        Connector connector = new DbConnector("databaseTest");
        Connection connection = connector.getConnection();
        assertNotNull(connection);
    }

    @Test
    void shouldThrowMissingResourceExceptionWhenIncorrectPropertyName() {
        Exception exception = assertThrows(MissingResourceException.class, () ->
            new DbConnector("incorrectName"));
        String actual = exception.getMessage();
        String expected = "Can't find bundle for base name incorrectName, locale en_US";
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowDaoExceptionWhenConnectionIncorrectParameters() {
        Connector connector = new DbConnector("incorrectDatabaseParameters");
        Exception exception = assertThrows(DaoException.class, connector::getConnection);
        String actual = exception.getMessage();
        String expected = "Connection is failed";
        assertEquals(expected, actual);
    }
}