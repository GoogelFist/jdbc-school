package ua.com.foxminded.school.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.school.dao.entity.Course;
import ua.com.foxminded.school.domain.TableCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseDaoImplTest {

    private static final String SQL_GET_ALL_COURSES = "SELECT * FROM courses";
    private static final String CREATE_TEST_TABLES_SCRIPT_PATH = "src/test/resources/scripts/createTablesTest.sql";

    private DbConnector connector;
    private CourseDao courseDao;
    private Course course1;
    private Course course2;
    private Course course3;
    private Course courseFromBase;

    @BeforeEach
    void setUp() {
        connector = new DbConnector("databaseTest");
        TableCreator creator = new TableCreator(connector);
        creator.createTables(CREATE_TEST_TABLES_SCRIPT_PATH);
        courseDao = new CourseDaoImpl(connector);
        course1 = new Course(1, "Arts", "Arts is course for leaning arts");
        course2 = new Course(2, "Law", "Law is course for leaning laws");
        course3 = new Course(3, "Engineering", "Engineering is course for leaning engineering");
    }

    @Test
    void shouldCreateCourseInDataBase() {
        courseDao.create(course1);

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_COURSES)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    courseFromBase = new Course(resultSet.getInt("id"),
                        resultSet.getString("name"), resultSet.getString("description"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Test for create course in database failed", e);
        }
        assertEquals(course1, courseFromBase);
    }

    @Test
    void shouldGetAllCoursesFromDataBase() {
        courseDao.create(course1);
        courseDao.create(course2);
        courseDao.create(course3);
        List<Course> allCoursesFromDb = new ArrayList<>();

        List<Course> expectedCourses = new ArrayList<>();
        expectedCourses.add(course1);
        expectedCourses.add(course2);
        expectedCourses.add(course3);

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_COURSES)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    allCoursesFromDb.add(new Course(resultSet.getInt("id"),
                        resultSet.getString("name"), resultSet.getString("description")));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Test for create course in database failed", e);
        }
        assertEquals(expectedCourses, allCoursesFromDb);
    }
}