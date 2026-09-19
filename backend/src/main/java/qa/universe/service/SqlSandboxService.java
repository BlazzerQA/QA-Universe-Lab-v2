package qa.universe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import qa.universe.dto.SqlQueryResponse;
import qa.universe.dto.SqlSchemaResponse;
import qa.universe.exception.SqlSandboxException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SqlSandboxService {

    private static final int MAX_ROWS = 100;
    private static final int QUERY_TIMEOUT_SECONDS = 5;

    private final JdbcTemplate jdbcTemplate;

    public SqlSchemaResponse schema() {
        return new SqlSchemaResponse("products", List.of("product_id", "product_name", "price"));
    }

    public SqlQueryResponse execute(String rawSql) {
        String sql = SqlSelectGuard.validate(rawSql);
        try {
            return jdbcTemplate.execute((java.sql.Connection connection) -> {
                try (Statement statement = connection.createStatement()) {
                    statement.setMaxRows(MAX_ROWS);
                    statement.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
                    try (ResultSet resultSet = statement.executeQuery(sql)) {
                        return mapResult(resultSet);
                    }
                }
            });
        } catch (SqlSandboxException e) {
            throw e;
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            throw new SqlSandboxException(sqlErrorMessage(cause), cause);
        }
    }

    private SqlQueryResponse mapResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData meta = resultSet.getMetaData();
        int columnCount = meta.getColumnCount();
        List<String> columns = new ArrayList<>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            columns.add(meta.getColumnLabel(i));
        }

        List<List<Object>> rows = new ArrayList<>();
        while (resultSet.next()) {
            List<Object> row = new ArrayList<>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                row.add(resultSet.getObject(i));
            }
            rows.add(row);
        }
        return new SqlQueryResponse(columns, rows, rows.size());
    }

    private String sqlErrorMessage(Throwable error) {
        String message = error.getMessage();
        if (message == null || message.isBlank()) {
            return "Не удалось выполнить запрос.";
        }
        return message;
    }
}
