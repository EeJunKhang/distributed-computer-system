package database;

import model.User;
import enums.UserRole;
import java.sql.*;
import java.util.List;
import model.Admin;
import model.Customer;

public class UserDAO extends DBOperation<User> {

    public UserDAO() {
        super("users", "first_name", "last_name", "username", "password_hash",
              "email", "address", "phone_number", "role"); 
    }

    @Override
    protected User mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("user_id"),                
            rs.getString("first_name"),          
            rs.getString("last_name"),           
            rs.getString("username"),
            rs.getString("password_hash"),       
            rs.getString("email"),
            rs.getString("address"),
            rs.getString("phone_number"),        
            UserRole.valueOf(rs.getString("role")),  
            rs.getString("created_at")     
        );
    }

    public void createUser(String firstName, String lastName, String username, String passwordHash,
                           String email, String address, String contactNumber, UserRole role) throws SQLException {
        insert(null, firstName, lastName, username, passwordHash, email, address, contactNumber, role.name());
    }


    public User getUserById(int userId) throws SQLException {
        List<User> users = searchByColumn("user_id", userId, false);  
        return users.isEmpty() ? null : users.get(0);  
    }
    
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = mapResultSetToEntity(rs);
               
                if (user.getRole() == UserRole.CUSTOMER) {
                    return new Customer(user.getUserId(), user.getFirstName(), user.getLastName(), 
                                        user.getUsername(), user.getPasswordHash(), user.getEmail(), 
                                        user.getAddress(), user.getContactNumber(), user.getCreatedTime());
                } else if (user.getRole() == UserRole.ADMIN) {
                    return new Admin(user.getUserId(), user.getFirstName(), user.getLastName(),
                                     user.getUsername(), user.getPasswordHash(), user.getEmail(),
                                     user.getAddress(), user.getContactNumber(), user.getCreatedTime());
                }
            }
        }
        return null;
    }
    
 
    public void updateUser(int userId, String firstName, String lastName, String username, String passwordHash,
                           String email, String address, String contactNumber, UserRole role) throws SQLException {
        update("user_id",userId, firstName, lastName, username, passwordHash, email, address, contactNumber, role.name());
    }
}
