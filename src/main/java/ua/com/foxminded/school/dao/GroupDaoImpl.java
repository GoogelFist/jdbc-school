package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.dao.entity.Group;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class GroupDaoImpl implements GroupDao {
    private static final String CREATE = "INSERT INTO groups (name) VALUES (?)";
    private static final String GET_BY_STUDENTS_COUNT = "SELECT groups.id, groups.name, COUNT (students.id) " +
        "FROM groups " +
        "JOIN students ON groups.id = students.group_id " +
        "GROUP BY groups.id " +
        "HAVING COUNT (students.id) <= ?";

    private final Connector connector;

    public GroupDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void create(Group group) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, group.getName());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    group.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error during creating", e);
        }
    }

    @Override
    public Map<String, Integer> getByStudentsCount(int count) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_STUDENTS_COUNT)) {

            statement.setInt(1, count);

            try (ResultSet resultSet = statement.executeQuery()) {
                Map<String, Integer> result = new HashMap<>();
                while (resultSet.next()) {
                    result.put(resultSet.getString("name"), resultSet.getInt("count"));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new DaoException("Read group by student count failed", e);
        }
    }
}