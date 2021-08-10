package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.dao.entity.Student;

import java.sql.*;

public class StudentDaoImpl implements StudentDao {

    private static final String CREATE = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String DELETE_STUDENT_BY_ID = "DELETE FROM students WHERE id = ?";

    private final Connector connector;

    public StudentDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void create(Student student) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {

            if (student.getGroupId() != 0) {
                statement.setInt(1, student.getGroupId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }

            statement.setString(2, student.getFistName());
            statement.setString(3, student.getLastName());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    student.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error during creating student", e);
        }
    }

    public void deleteById(int studentId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT_BY_ID)) {

            statement.setInt(1, studentId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Delete student by id failed", e);
        }
    }
}