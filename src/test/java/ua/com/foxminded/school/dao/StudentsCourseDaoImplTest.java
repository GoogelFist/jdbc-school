package ua.com.foxminded.school.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.school.dao.entity.Course;
import ua.com.foxminded.school.dao.entity.Student;
import ua.com.foxminded.school.dao.entity.StudentCourse;
import ua.com.foxminded.school.domain.Creator;
import ua.com.foxminded.school.domain.TableCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentsCourseDaoImplTest {

    private static final String SQL_GET_ALL_STUDENTS_COURSES = "SELECT * FROM students_courses";
    private static final String CREATE_TEST_TABLES_SCRIPT_PATH = "src/test/resources/scripts/createTablesTest.sql";

    private Connector connector;
    private StudentsCourseDao studentsCourseDao;
    private StudentCourse studentCourse1;
    private StudentCourse studentCourse2;
    private StudentCourse studentCourseFromBase;


    @BeforeEach
    void setUp() {
        connector = new DbConnector("databaseTest");
        Creator creator = new TableCreator(connector);
        creator.createTables(CREATE_TEST_TABLES_SCRIPT_PATH);

        StudentDao studentDao = new StudentDaoImpl(connector);
        CourseDao courseDao = new CourseDaoImpl(connector);
        studentsCourseDao = new StudentsCourseDaoImpl(connector);

        Student student = new Student(1, 0, "Tom", "Atkins");
        Course course1 = new Course(1, "Arts", "Is course for learning arts");
        Course course2 = new Course(2, "Music", "Is course for learning Music");
        studentCourse1 = new StudentCourse(1, 1);
        studentCourse2 = new StudentCourse(1, 2);

        studentDao.create(student);
        courseDao.create(course1);
        courseDao.create(course2);
    }

    @Test
    void shouldCreateStudentsCoursesInDataBase() {
        studentsCourseDao.create(studentCourse1);

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_STUDENTS_COURSES)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    studentCourseFromBase = new StudentCourse(resultSet.getInt("student_id"),
                        resultSet.getInt("course_id"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Test for create studentsCourses in database failed", e);
        }
        assertEquals(studentCourse1, studentCourseFromBase);
    }

    @Test
    void shouldGetStudentsRelatedToCourse() {
        studentsCourseDao.create(studentCourse1);
        List<String> studentsInArtsCourse = studentsCourseDao.getStudentsRelatedToCourse("Arts");
        String actual = studentsInArtsCourse.get(0);
        String expected = "Tom Atkins";

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetCoursesByStudentId() {
        studentsCourseDao.create(studentCourse1);
        Map<Integer, String> actual = studentsCourseDao.getCoursesByStudentId(1);
        Map<Integer, String> expected = new HashMap<>();
        expected.put(1, "Arts");

        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteCourseById() {
        studentsCourseDao.create(studentCourse1);
        studentsCourseDao.create(studentCourse2);
        studentsCourseDao.deleteCourseById(1, 2);

        List<StudentCourse> studentsCourses = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_STUDENTS_COURSES)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    studentsCourses.add(new StudentCourse(resultSet.getInt("student_id"),
                        resultSet.getInt("course_id")));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Test for delete student by id failed", e);
        }

        int actual = studentsCourses.size();
        int expected = 1;

        assertEquals(actual, expected);
    }
}