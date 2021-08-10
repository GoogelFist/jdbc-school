package ua.com.foxminded.school.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.school.dao.entity.Student;
import ua.com.foxminded.school.domain.Creator;
import ua.com.foxminded.school.domain.TableCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentDaoImplTest {

    private static final String SQL_GET_ALL_STUDENTS = "SELECT * FROM students";
    private static final String CREATE_TEST_TABLES_SCRIPT_PATH = "src/test/resources/scripts/createTablesTest.sql";

    private Connector connector;
    private StudentDao studentDao;
    private Student student;
    private Student student1;
    private Student studentFromBase;


    @BeforeEach
    void setUp() {
        connector = new DbConnector("databaseTest");
        Creator creator = new TableCreator(connector);
        creator.createTables(CREATE_TEST_TABLES_SCRIPT_PATH);
        studentDao = new StudentDaoImpl(connector);
        student = new Student(1, 0, "Tomas", "Patty");
        student1 = new Student(2, 0, "Tom", "Pain");
    }

    @Test
    void shouldCreateStudentInDataBase() {
        studentDao.create(student);

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_STUDENTS)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    studentFromBase = new Student(resultSet.getInt("id"),
                        resultSet.getInt("group_id"),
                        resultSet.getString("first_name"), resultSet.getString("last_name"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Test for create student in database failed", e);
        }
        assertEquals(student, studentFromBase);
    }

    @Test
    void shouldDeleteStudentFromDataBase() {
        studentDao.create(student);
        studentDao.create(student1);
        studentDao.deleteById(2);

        List<Student> students = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_STUDENTS)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(new Student(resultSet.getInt("id"), resultSet.getInt("group_id"),
                        resultSet.getString("first_name"), resultSet.getString("last_name")));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Test for delete student from database failed", e);
        }

        int actual = students.size();
        int expected = 1;

        assertEquals(actual, expected);
    }
}