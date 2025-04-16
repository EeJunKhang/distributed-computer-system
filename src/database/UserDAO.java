package database;

import enums.UserRole;
import model.User;
import model.Customer;
import model.Admin;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entities
 */
public class UserDAO extends DBOperation<User, Integer> {

    /**
     * Default constructor
     */
    public UserDAO() {
        super();
    }

    /**
     * Helper method to create a User object from a ResultSet
     *
     * @param rs The ResultSet
     * @return The User object
     * @throws SQLException If a database error occurs
     */
    @Override
    protected User mapResultSetToEntity(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String username = rs.getString("username");
        String passwordHash = rs.getString("password_hash");
        String passwordSalt = rs.getString("password_salt");
        String email = rs.getString("email");
        String address = rs.getString("address");
        String contactNumber = rs.getString("contact_number");
        LocalDateTime createdTime = rs.getTimestamp("created_time").toLocalDateTime();
        UserRole role = UserRole.valueOf(rs.getString("role"));

        // Create the appropriate user type based on role
        User user;
        if (role == UserRole.ADMIN) {
            user = new Admin(userId, firstName, lastName, username, passwordHash, passwordSalt,
                    email, address, contactNumber, createdTime);
        } else {
            user = new Customer(userId, firstName, lastName, username, passwordHash, passwordSalt,
                    email, address, contactNumber, createdTime);
        }

        return user;
    }

    @Override
    public boolean create(User user) {
        String sql = "INSERT INTO users (first_name, last_name, username, password_hash, password_salt, "
                + "email, address, contact_number, created_time, role) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, user.getFirstName());
                stmt.setString(2, user.getLastName());
                stmt.setString(3, user.getUsername());
                stmt.setString(4, user.getPasswordHash());
                stmt.setString(5, user.getPasswordSalt());
                stmt.setString(6, user.getEmail());
                stmt.setString(7, user.getAddress());
                stmt.setString(8, user.getContactNumber());
                stmt.setTimestamp(9, Timestamp.valueOf(user.getCreatedTime()));
                stmt.setString(10, user.getRole().toString());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    return false;
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        });
    }

    /**
     * Alternative method name for create - for backward compatibility
     *
     * @param user The user to add
     * @return True if successful, false otherwise
     */
    public boolean addUser(User user) {
        return create(user);
    }

    @Override
    public User read(Integer userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
//        System.out.println("Attempting to read user ID: " + userId);

        return executeTransaction(conn -> {
//            System.out.println("Got connection, preparing statement");
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
//                System.out.println("Executing query");

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
//                        System.out.println("Found user, mapping result");
                        return mapResultSetToEntity(rs);
                    }
                    System.out.println("No user found with ID: " + userId);
                    return null;
                }
            }
        });
    }

    /**
     * Alternative method name for read - for backward compatibility
     *
     * @param userId The user ID
     * @return The user or null if not found
     */
    public User getUserById(int userId) {
        return read(userId);
    }
 
    /**
     * Get a user by username
     *
     * @param username The username
     * @return The user or null if not found
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                    return null;
                }
            }
        });
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, "
                + "address = ?, contact_number = ? WHERE user_id = ?";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getFirstName());
                stmt.setString(2, user.getLastName());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getAddress());
                stmt.setString(5, user.getContactNumber());
                stmt.setInt(6, user.getUserId());

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        });
    }

    /**
     * Update a user's password
     *
     * @param userId The user ID
     * @param passwordHash The new password hash
     * @param passwordSalt The new password salt
     * @return True if successful, false otherwise
     */
    public boolean updatePassword(int userId, String passwordHash, String passwordSalt) {
        String sql = "UPDATE users SET password_hash = ?, password_salt = ? WHERE user_id = ?";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, passwordHash);
                stmt.setString(2, passwordSalt);
                stmt.setInt(3, userId);

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        });
    }

    @Override
    public boolean delete(Integer userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        });
    }

    /**
     * Alternative method name for delete - for backward compatibility
     *
     * @param userId The user ID
     * @return True if successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        return delete(userId);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";

        return executeTransaction(conn -> {
            List<User> users = new ArrayList<>();

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    User user = mapResultSetToEntity(rs);
                    users.add(user);
                }

                return users;
            }
        });
    }

    /**
     * Alternative method name for getAll - for backward compatibility
     *
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return getAll();
    }

    /**
     * Get users by role
     *
     * @param role The role to filter by
     * @return List of users with the specified role
     */
    public List<User> getUsersByRole(UserRole role) {
        String sql = "SELECT * FROM users WHERE role = ?";

        return executeTransaction(conn -> {
            List<User> users = new ArrayList<>();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, role.toString());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        User user = mapResultSetToEntity(rs);
                        users.add(user);
                    }

                    return users;
                }
            }
        });
    }

}
