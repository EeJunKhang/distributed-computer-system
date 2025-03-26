package database;

import java.util.Collections;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DBOperation<T> {

    protected String tableName;
    protected String[] columns;

    public DBOperation(String tableName, String... columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    //create - insert data
    public void insert(T entity, Object... values) throws SQLException {
        String placeholders = String.join(", ", Collections.nCopies(values.length, "?"));
        String sql = "INSERT INTO " + tableName + " ("
                + String.join(", ", columns) + ") VALUES (" + placeholders + ")";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }

            stmt.executeUpdate();
        }
    }

    //read by keyword
    public List<T> searchByColumn(String column, Object keyword, boolean isString) throws SQLException {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE " + column + (isString ? " LIKE ?" : " = ?");

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (isString) {
                stmt.setObject(1, "%" + keyword + "%");
            } else {
                stmt.setObject(1, keyword);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
        }
        return results;
    }

    //Read all
    public List<T> readAll() throws SQLException {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
        }
        return results;
    }

    //update
    public void update(String columnName, int id, Object... values) throws SQLException {

        String setClause = String.join(" = ?, ",
                Arrays.copyOfRange(columns, 0, columns.length)) + " = ?";

        String sql = "UPDATE " + tableName + " SET " + setClause
                + " WHERE " + columnName + " = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }

            stmt.setInt(values.length + 1, id);

            stmt.executeUpdate();
        }
    }

    //delete data by id
    public void deleteById(int id, String column_name) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE " + column_name + " = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
}
