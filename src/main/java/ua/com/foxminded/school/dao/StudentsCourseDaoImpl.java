package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.dao.entity.StudentCourse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentsCourseDaoImpl implements StudentsCourseDao {
    private static final String CREATE = "INSERT INTO students_courses (student_id, course_id) VALUES (?, ?)";

    private static final String GET_STUDENTS_RELATED_TO_COURSE =
        "SELECT CONCAT (students.first_name, ' ', students.last_name) AS full_name " +
            "FROM students " +
            "JOIN students_courses sc on students.id = sc.student_id " +
            "JOIN courses c on sc.course_id = c.id " +
            "WHERE name = ?";

    private static final String GET_COURSES_BY_STUDENT_ID = "SELECT c.id AS course_id, c.name AS course_name " +
        "FROM students_courses " +
        "JOIN courses c on students_courses.course_id = c.id " +
        "WHERE student_id = ?";

    private static final String DELETE_COURSE_BY_ID = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?";

    private final Connector connector;

    public StudentsCourseDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void create(StudentCourse studentCourse) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE)) {

            statement.setInt(1, studentCourse.getStudentId());
            statement.setInt(2, studentCourse.getCourseId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error during creating", e);
        }
    }

    public List<String> getStudentsRelatedToCourse(String courseName) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_STUDENTS_RELATED_TO_COURSE)) {

            statement.setString(1, courseName);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<String> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(resultSet.getString("full_name"));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new DaoException("Read students related to course failed", e);
        }
    }

    public Map<Integer, String> getCoursesByStudentId(int id) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_COURSES_BY_STUDENT_ID)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                Map<Integer, String> result = new HashMap<>();
                while (resultSet.next()) {
                    result.put(resultSet.getInt("course_id"), resultSet.getString("course_name"));
                }
                return result;
            }
        } catch (
            SQLException e) {
            throw new DaoException("Read courses by student id failed", e);
        }
    }

    public void deleteCourseById(int studentId, int courseId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_COURSE_BY_ID)) {

            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Delete course by id failed", e);
        }
    }
}