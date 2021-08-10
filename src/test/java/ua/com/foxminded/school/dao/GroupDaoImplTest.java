package ua.com.foxminded.school.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.school.dao.entity.Group;
import ua.com.foxminded.school.dao.entity.Student;
import ua.com.foxminded.school.domain.Creator;
import ua.com.foxminded.school.domain.TableCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDaoImplTest {

    private static final String SQL_GET_ALL_GROUPS = "SELECT * FROM groups";
    private static final String CREATE_TEST_TABLES_SCRIPT_PATH = "src/test/resources/scripts/createTablesTest.sql";

    private Connector connector;
    private GroupDao groupDao;
    private Group group;
    private Group groupFromBase;

    @BeforeEach
    void setUp() {
        connector = new DbConnector("databaseTest");
        Creator creator = new TableCreator(connector);
        creator.createTables(CREATE_TEST_TABLES_SCRIPT_PATH);
        groupDao = new GroupDaoImpl(connector);
        group = new Group(1, "aa - 11");
    }

    @Test
    void shouldCreateGroupInDataBase() {
        groupDao.create(group);

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_GROUPS)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    groupFromBase = new Group(resultSet.getInt("id"),
                        resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Test for create groups in database failed", e);
        }
        assertEquals(group, groupFromBase);
    }

    @Test
    void shouldGetStudentsCount() {
        groupDao.create(group);
        StudentDao studentDao = new StudentDaoImpl(connector);
        studentDao.create(new Student(1, 1, "Tomas", "Filter"));
        studentDao.create(new Student(2, 1, "Henry", "Stone"));
        Map<String, Integer> actual = groupDao.getByStudentsCount(10);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("aa - 11", 2);

        assertEquals(expected, actual);
    }
}