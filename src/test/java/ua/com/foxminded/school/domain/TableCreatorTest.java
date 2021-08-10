package ua.com.foxminded.school.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.school.dao.DaoException;
import ua.com.foxminded.school.dao.DbConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TableCreatorTest {

    private static final String CREATE_TEST_TABLES_SCRIPT_PATH = "src/test/resources/scripts/createTablesTest.sql";
    private static final String PATH_FOR_INCORRECT_SCRIPT = "src/test/resources/scripts/incorrect.sql";
    private static final String CREATE_TABLES_TEST = "SELECT table_schema, COUNT(table_schema) " +
        "FROM school_test.information_schema.tables " +
        "WHERE table_schema = 'public' " +
        "GROUP BY table_schema;";

    private DbConnector connector;
    private TableCreator creator;


    @BeforeEach
    void setUp() {
        connector = new DbConnector("databaseTest");
    }

    @Test
    void shouldCreateTables() {
        creator = new TableCreator(connector);
        creator.createTables(CREATE_TEST_TABLES_SCRIPT_PATH);
        int actualCountTables = 0;

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLES_TEST)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    actualCountTables = resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("", e);
        }
        int expectedCountTables = 4;
        assertEquals(expectedCountTables, actualCountTables);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIncorrectScriptPath() {
        creator = new TableCreator(connector);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            creator.createTables("incorrectPath"));
        String actual = exception.getMessage();
        String expected = "File not found";
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowDaoExceptionWhenExecuteIncorrectScript() {
        creator = new TableCreator(connector);
        Exception exception = assertThrows(DaoException.class, () -> creator.createTables(PATH_FOR_INCORRECT_SCRIPT));

        String actual = exception.getMessage();
        String expected = "Unable to execute script";
        assertEquals(expected, actual);
    }
}