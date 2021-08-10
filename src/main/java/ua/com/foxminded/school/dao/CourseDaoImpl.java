package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.dao.entity.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements CourseDao {

    private static final String CREATE = "INSERT INTO courses (name, description) VALUES (?, ?)";
    private static final String READ_ALL = "SELECT * FROM courses";

    private final Connector connector;

    public CourseDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void create(Course course) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    course.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error during creating", e);
        }
    }

    @Override
    public List<Course> getAll() {

        try (Connection connection = connector.getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet resultSet = statement.executeQuery(READ_ALL)) {
                List<Course> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(new Course(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new DaoException("Read all courses failed", e);
        }
    }
}