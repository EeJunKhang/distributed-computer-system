
package database;

import enums.UserRole;
import model.User;

import java.sql.*;

public class UserDAO {
    public void createUser(User user) throws SQLException{
         String sql = "INSERT INTO users (first_name, last_name, username, password_hash, email, address, phone_number, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
         try (Connection conn = DBConnection.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setInt(1, user.getUserId());
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPasswordHash());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getContactNumber());
            stmt.setString(8, user.getRole().toString());
            stmt.executeUpdate();
         }
    }
    
    public User getUserById(int userId) {
    User user = null;
    String sql = "SELECT * FROM users WHERE user_id = ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            UserRole role = UserRole.valueOf(rs.getString("role"));
            user = new User(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getString("phone_number"),
                role,
                rs.getString("created_at")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return user;
}

}
