package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;

import database.DBOperation.DatabaseTransaction;
import manager.DatabaseManager;

/**
 * Abstract class for database operations
 * Provides a template for all DAO classes to implement
 * @param <T> The entity type this operation works with
 * @param <K> The primary key type of the entity
 */
public abstract class DBOperation<T, K> {
    
    protected final DatabaseManager dbManager;
    
    /**
     * Constructor - initializes database manager
     */
    public DBOperation() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if a database error occurs
     */
    protected Connection getConnection() throws SQLException {
        return dbManager.getConnection();
    }
    
    /**
     * Create a new entity in the database
     * @param entity The entity to create
     * @return true if successful, false otherwise
     */
    public abstract boolean create(T entity);
    
    /**
     * Read an entity from the database by its primary key
     * @param id The primary key
     * @return The entity or null if not found
     */
    public abstract T read(K id);
    
    /**
     * Update an existing entity in the database
     * @param entity The entity to update
     * @return true if successful, false otherwise
     */
    public abstract boolean update(T entity);
    
    /**
     * Delete an entity from the database
     * @param id The primary key of the entity to delete
     * @return true if successful, false otherwise
     */
    public abstract boolean delete(K id);
    
    /**
     * Get all entities from the database
     * @return List of all entities
     */
    public abstract List<T> getAll();
    
    /**
     * Method to map the ResultSet to an entity. Must be implemented by subclasses.
     * @param rs The ResultSet
     * @return The mapped entity
     * @throws SQLException If a database error occurs
     */
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    
    /**
     * Execute a database operation with optional transaction handling.
     * --
     * If {@code transactional} is {@code true}, the method disables auto-commit,
     * executes the transaction, and manually commits the result. If any exception occurs,
     * it rolls back the transaction to maintain data integrity.
     * --
     * If {@code transactional} is {@code false}, the method executes the operation using
     * the default auto-commit behaviour of the database connection.
     * --
     * @param <R> The type of the result returned by the transaction
     * @param transactional Whether to wrap the operation in a manual transaction
     * @param transaction The database operation to execute
     * @return The result of the transaction, or {@code null} if an exception occurred
     */
    protected <R> R executeTransaction(boolean transactional, DatabaseTransaction<R> transaction) {
        Connection conn = null;
        boolean originalAutoCommit = true;
    
        try {
            conn = getConnection();
            originalAutoCommit = conn.getAutoCommit();
    
            if (transactional) {
                conn.setAutoCommit(false);
            }
    
            R result = transaction.execute(conn);
    
            if (transactional) {
                conn.commit();
                System.out.println("Transaction committed successfully.");
            }
    
            return result;
        } catch (SQLException e) {
            System.out.println("SQL Exception in transaction: " + e.getMessage());
            if (transactional && conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Rollback successful");
                } catch (SQLException rollbackEx) {
                    System.out.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
            if (transactional && conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Rollback successful");
                } catch (SQLException rollbackEx) {
                    System.out.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (conn != null) {
                    if (transactional) {
                        conn.setAutoCommit(originalAutoCommit);
                    }
                    conn.close();
                    System.out.println("Connection closed.");
                }
            } catch (SQLException e) {
                System.out.println("Failed to close connection: " + e.getMessage());
            }
        }
    }
    
    protected <R> R executeTransaction(DatabaseTransaction<R> transaction) {
        return executeTransaction(false, transaction); 
        // false = default to non-transactional (auto-commit mode)
    }
    

    /**
     * Functional interface for database transactions
     * @param <R> The return type of the transaction
     */
    @FunctionalInterface
    protected interface DatabaseTransaction<R> {
        R execute(Connection conn) throws SQLException;
    }
    
    /**
     * Log a database operation error
     * @param operation The operation being performed
     * @param error The error message
     * @param exception The exception
     */
    protected void logError(String operation, String error, Exception exception) {
        System.err.println("Database error during " + operation + ": " + error);
    }
}
