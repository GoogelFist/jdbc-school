package ua.com.foxminded.school.domain;

import ua.com.foxminded.school.dao.Connector;
import ua.com.foxminded.school.dao.DaoException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Stream;

public class TableCreator implements Creator {

    private final Connector connector;

    public TableCreator(Connector connector) {
        this.connector = connector;
    }

    public void createTables(String pathSqlScript) {
        String tableBuilderScript = getScriptFromFile(pathSqlScript);
        executeScript(tableBuilderScript);
    }

    private String getScriptFromFile(String pathSqlScript) {
        StringBuilder builder = new StringBuilder();
        Path path = Paths.get(pathSqlScript);

        try (Stream<String> lineStream = Files.newBufferedReader(path).lines()) {
            lineStream.forEach(builder::append);
            return builder.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException("File not found", e);
        }
    }

    private void executeScript(String sqlScript) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlScript)) {
            statement.execute();
        } catch (SQLException e) {
            throw new DaoException("Unable to execute script", e);
        }
    }
}