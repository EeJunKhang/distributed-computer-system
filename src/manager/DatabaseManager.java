/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author C
 */

package manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Singleton class for managing database connections
 * Provides centralized connection handling for all DAO classes
 */
public class DatabaseManager {
    
    private static DatabaseManager instance;
    private static final ReentrantLock lock = new ReentrantLock();
    
    private String jdbcUrl;
    private String username;
    private String password;
    private int maxConnections;
    private boolean useConnectionPool;
    
    // Connection pool if enabled
    private ConnectionPool connectionPool;
    
    /**
     * Private constructor - loads database configuration
     */
    private DatabaseManager() {
        loadConfiguration();
        
        if (useConnectionPool) {
            connectionPool = new ConnectionPool(jdbcUrl, username, password, maxConnections);
        }
    }
    
    /**
     * Get the singleton instance of DatabaseManager
     * @return The DatabaseManager instance
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }
    
    /**
     * Load database configuration from properties file
     */
    private void loadConfiguration() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("src/config/DatabaseProperties.properties")) {
            props.load(fis);
            
            jdbcUrl = props.getProperty("jdbc.url", "jdbc:mysql://localhost:3306/mydb");
            username = props.getProperty("jdbc.username", "root");
            password = props.getProperty("jdbc.password", "");
            maxConnections = Integer.parseInt(props.getProperty("jdbc.max_connections", "10"));
            useConnectionPool = Boolean.parseBoolean(props.getProperty("jdbc.use_connection_pool", "true"));
            
            Class.forName(props.getProperty("jdbc.driver", "com.mysql.cj.jdbc.Driver"));
            
        } catch (IOException e) {
            System.err.println("Warning: Could not load database configuration file. Using defaults.");
            
            jdbcUrl = "jdbc:mysql://localhost:3306/mydb";
            username = "root";
            password = "root";
            maxConnections = 10;
            useConnectionPool = true;
            
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.err.println("Error: JDBC driver not found.");
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC driver not found.");
            e.printStackTrace();
        }
    }
    
    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if a database error occurs
     */
    public Connection getConnection() throws SQLException {
        if (useConnectionPool) {
            return connectionPool.getConnection();
        } else {
            return DriverManager.getConnection(jdbcUrl, username, password);
        }
    }
    
    /**
     * Release a connection back to the pool (if using pool)
     * @param connection The connection to release
     */
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                if (useConnectionPool) {
                    connectionPool.releaseConnection(connection);
                } else {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Shutdown the database manager and release all resources
     */
    public void shutdown() {
        if (useConnectionPool && connectionPool != null) {
            connectionPool.shutdown();
        }
    }
    
    /**
     * Test connection to the database
     * @return true if connection successful, false otherwise
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Simple connection pool implementation
     */
    private class ConnectionPool {
        private final String url;
        private final String user;
        private final String password;
        private final int maxSize;
        
        private final java.util.Queue<Connection> connections = new java.util.LinkedList<>();
        private int currentSize = 0;
        private final ReentrantLock poolLock = new ReentrantLock();
        
        /**
         * Constructor - initializes the connection pool
         */
        public ConnectionPool(String url, String user, String password, int maxSize) {
            this.url = url;
            this.user = user;
            this.password = password;
            this.maxSize = maxSize;
        }
        
        /**
         * Get a connection from the pool or create a new one if needed
         * @return Connection object
         * @throws SQLException if a database error occurs
         */
        public Connection getConnection() throws SQLException {
            poolLock.lock();
            try {
                if (!connections.isEmpty()) {
                    Connection conn = connections.poll();
                    
                    if (conn.isValid(1)) {
                        return conn;
                    } else {
                        currentSize--;
                        return createConnection();
                    }
                } else if (currentSize < maxSize) {
                    return createConnection();
                } else {
                    throw new SQLException("Connection pool exhausted");
                }
            } finally {
                poolLock.unlock();
            }
        }
        
        /**
         * Create a new database connection
         * @return Connection object
         * @throws SQLException if a database error occurs
         */
        private Connection createConnection() throws SQLException {
            Connection conn = DriverManager.getConnection(url, user, password);
            currentSize++;
            return conn;
        }
        
        /**
         * Release a connection back to the pool
         * @param conn The connection to release
         */
        public void releaseConnection(Connection conn) {
            poolLock.lock();
            try {
                if (conn != null && !conn.isClosed()) {
                    connections.offer(conn);
                } else {
                    currentSize--;
                }
            } catch (SQLException e) {
                currentSize--;
                System.err.println("Error releasing connection: " + e.getMessage());
            } finally {
                poolLock.unlock();
            }
        }
        
        /**
         * Shutdown the connection pool
         */
        public void shutdown() {
            poolLock.lock();
            try {
                for (Connection conn : connections) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        System.err.println("Error closing connection during shutdown: " + e.getMessage());
                    }
                }
                connections.clear();
                currentSize = 0;
            } finally {
                poolLock.unlock();
            }
        }
    }
}