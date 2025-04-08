package manager;

import database.UserDAO;
import enums.UserRole;
import model.User;
import model.Customer;
import model.Admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager class for User-related operations
 * TODO: FIX Change password & Promote to admin
 */
public class UserManager {

    private static UserManager instance;
    private final UserDAO userDAO;
    private final AuthManager authManager;

    /**
     * Private constructor for singleton pattern
     */
    private UserManager() {
        this.userDAO = new UserDAO();
        this.authManager = AuthManager.getInstance();
    }

    /**
     * Get singleton instance
     * @return The UserManager instance
     */
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /**
     * Get a user by ID
     * @param userId The user ID
     * @return The user or null if not found
     */
    public User getUserById(int userId) {
        return userDAO.read(userId);
    }

    /**
     * Get a user by username
     * @param username The username
     * @return The user or null if not found
     */
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    /**
     * Get all customers
     * @return List of all customers
     */
    public List<Customer> getAllCustomers() {
        List<User> allUsers = userDAO.getUsersByRole(UserRole.CUSTOMER);
        return allUsers.stream()
                .filter(user -> user instanceof Customer)
                .map(user -> (Customer) user)
                .collect(Collectors.toList());
    }

    /**
     * Get all admins
     * @return List of all admins
     */
    public List<Admin> getAllAdmins() {
        List<User> allUsers = userDAO.getUsersByRole(UserRole.ADMIN);
        return allUsers.stream()
                .filter(user -> user instanceof Admin)
                .map(user -> (Admin) user)
                .collect(Collectors.toList());
    }

    /**
     * Update a user's profile
     * @param tokenString Authentication token string
     * @param userId ID of the user to update
     * @param firstName New first name (or null to keep existing)
     * @param lastName New last name (or null to keep existing)
     * @param email New email (or null to keep existing)
     * @param address New address (or null to keep existing)
     * @param contactNumber New contact number (or null to keep existing)
     * @return true if successful, false if failed or unauthorized
     */
    public boolean updateUserProfile(String tokenString, int userId, String firstName,
                                        String lastName, String email, String address,
                                        String contactNumber) {
        User tokenUser = authManager.getUserByToken(tokenString);
        if (tokenUser == null) {
            return false;
        }

        // Only admins or the user themselves
        if (tokenUser.getUserId() != userId && tokenUser.getRole() != UserRole.ADMIN) {
            return false;
        }

        User user = userDAO.read(userId);
        if (user == null) {
            return false;
        }

        // Update only provided fields
        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);
        if (email != null) user.setEmail(email);
        if (address != null) user.setAddress(address);
        if (contactNumber != null) user.setContactNumber(contactNumber);

        return userDAO.update(user);
    }

    /**
     * Change a user's password
     * @param tokenString Authentication token string
     * @param userId ID of the user to update
     * @param currentPassword Current password (required for validation)
     * @param newPassword New password
     * @return true if successful, false if failed or unauthorized
     */
    public boolean changePassword(String tokenString, int userId, String currentPassword,
                                 String newPassword) {
        User tokenUser = authManager.getUserByToken(tokenString);
        if (tokenUser == null) {
            return false;
        }

        if (tokenUser.getUserId() != userId) {
            return false;
        }

        User user = userDAO.read(userId);
        if (user == null) {
            return false;
        }

        if (!authManager.verifyPassword(currentPassword, user.getPasswordHash(), user.getPasswordSalt())) {
            return false;
        }

        String salt = authManager.generateSalt();
        String passwordHash = authManager.hashPassword(newPassword, salt);

        return userDAO.updatePassword(userId, passwordHash, salt);
    }

    /**
     * Delete a user account (admin only)
     * @param tokenString Authentication token string
     * @param userId ID of the user to delete
     * @return true if successful, false if failed or unauthorized
     */
    public boolean deleteUser(String tokenString, int userId) {
        // Check if token is valid and get the associated user
        User tokenUser = authManager.getUserByToken(tokenString);
        if (tokenUser == null || tokenUser.getRole() != UserRole.ADMIN) {
            return false;
        }

        User user = userDAO.read(userId);
        if (user == null) {
            return false;
        }

        // Don't allow deletion of the user performing the action
        if (user.getUserId() == tokenUser.getUserId()) {
            return false;
        }

        return userDAO.delete(userId);
    }

    /**
     * Create a new user account
     * @param newUser The user object to create
     * @return true if successful, false if failed
     */
    public boolean createUser(User newUser) {
        if (newUser.getCreatedTime() == null) {
            newUser.setCreatedTime(LocalDateTime.now());
        }

        return userDAO.create(newUser);
    }

    /**
     * Promote a customer to admin status (admin only)
     * @param tokenString Authentication token string
     * @param userId ID of the user to promote
     * @return true if successful, false if failed or unauthorized
     */
    public boolean promoteToAdmin(String tokenString, int userId) {
        User tokenUser = authManager.getUserByToken(tokenString);
        if (tokenUser == null || tokenUser.getRole() != UserRole.ADMIN) {
            return false;
        }

        User user = userDAO.read(userId);
        if (user == null || user.getRole() == UserRole.ADMIN) {
            return false;
        }

        // Create a new Admin object
        Admin admin = new Admin(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getPasswordSalt(),
                user.getEmail(),
                user.getAddress(),
                user.getContactNumber(),
                user.getCreatedTime()
        );

        return userDAO.update(admin);
    }
}