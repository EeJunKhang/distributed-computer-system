package manager;

import client.AuthResult;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;

import database.UserDAO;
import enums.UserRole;
import model.User;
import model.Customer;

public class AuthManager {

    private static AuthManager instance;
    private final UserDAO userDAO;
    private final Map<String, SessionInfo> activeSessions;

    // Token expiration time (60 minutes in milliseconds)
    private static final long TOKEN_EXPIRATION = 60 * 60 * 1000;

    private AuthManager() {
        userDAO = new UserDAO();
        activeSessions = new HashMap<>();
    }

    public static synchronized AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    /**
     * Authenticates a user and returns a token
     *
     * @param username Username
     * @param password The plain-text password
     * @return Authentication token OR null
     */
    public String authenticateUser(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            System.out.println("User not found: " + username);
            return null;
        }
        if (verifyPassword(password, user.getPasswordHash(), user.getPasswordSalt())) {

            String token = generateToken();

            SessionInfo session = new SessionInfo(user.getUserId(), user.getRole(), System.currentTimeMillis());
            activeSessions.put(token, session);

            System.out.println("User authenticated: " + user.getUsername() + user.getRole().toString());
            return token;
        } else {
            System.out.println("Invalid password for user: " + username);
            return null;
        }
    }

    /**
     * Registers a new user
     *
     * @param firstName User's first name
     * @param lastName User's last name
     * @param username Username
     * @param password Plain-text password (will be hashed)
     * @param email User's email
     * @param address User's address
     * @param contactNumber User's contact number
     * @param isAdmin Whether the user should be registered as admin
     * @return Boolean indicating success
     */
    public AuthResult registerUser(String firstName, String lastName, String username,
            String password, String email, String address,
            String contactNumber, boolean isAdmin) {
        try {
            // Check duplicate username
            if (userDAO.getUserByUsername(username) != null) {
                return new AuthResult("Duplicate Username Found");
            }

            System.out.println(password.length());

            if (password.length() < 6) {
                return new AuthResult("Password Length is less than 6");
            }

            if (!email.contains("@")) {
                return new AuthResult("Email is Invalid");
            }

            String salt = generateSalt();
            String passwordHash = hashPassword(password, salt);

            User user;
            LocalDateTime createdTime = LocalDateTime.now();

            if (isAdmin) {
                System.out.println("Error: Admin User Creation Prohibited!");
                return new AuthResult("Error: Admin User Creation Prohibited!");

            } else {
                user = new Customer(firstName, lastName, username, passwordHash, salt,
                        email, address, contactNumber, createdTime);

                boolean result = userDAO.addUser(user);

                if (result) {
                    System.out.println("User registered: " + user.getUsername());
                } else {
                    System.out.println("Failed to register user: " + user.getUsername());
                    return new AuthResult("Error registering");
                }

                return new AuthResult();
            }
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            return new AuthResult("Error registering");
        }
    }

    /**
     * Validates if a token is valid and not expired
     *
     * @param token The authentication token
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        if (token == null || !activeSessions.containsKey(token)) {
            System.out.println("Token is null or not in active sessions");
            return false;
        }
        SessionInfo session = activeSessions.get(token);
        long currentTime = System.currentTimeMillis();
        long age = currentTime - session.getCreationTime();

        if (age > TOKEN_EXPIRATION) {
            activeSessions.remove(token);
            System.out.println("Token expired and removed");
            return false;
        }
        return true;
    }

    /**
     * Gets the user role associated with a valid token
     *
     * @param token The authentication token
     * @return UserRole or null if the token is invalid
     */
    public UserRole getUserRole(String token) {
        if (validateToken(token)) {
            return activeSessions.get(token).getRole();
        }
        return null;
    }

    /**
     * Gets the user ID associated with a valid token
     *
     * @param token The authentication token
     * @return The user ID or -1 if the token is invalid
     */
    // Currently Not in RMI Interface
    public int getUserId(String token) {
        if (validateToken(token)) {
            return activeSessions.get(token).getUserId();
        }
        return -1;
    }

    /**
     * Gets the full user object associated with a valid token
     *
     * @param token The authentication token
     * @return The User object or null if the token is invalid
     */
    public User getUserByToken(String token) {
        if (validateToken(token)) {
            int userId = activeSessions.get(token).getUserId();
            System.out.println(userId);
            return userDAO.getUserById(userId);
        }
        System.out.println("tttttt2");
        return null;
    }
//    public User getUserByToken(String token) {
//        if (!validateToken(token)) {
//            return null;
//        }
//
//        int userId = activeSessions.get(token).getUserId();
//        System.out.println("Token valid, user ID: " + userId);
//
//        try {
//            // Add timeout to prevent hanging indefinitely
//            return userDAO.getUserByIdAsync(userId)
//                    .get(5, TimeUnit.SECONDS); // Timeout after 5 seconds
//        } catch (TimeoutException e) {
//            System.err.println("Timeout fetching user data for ID: " + userId);
//            return null;
//        } catch (Exception e) {
//            System.err.println("Error fetching user data: " + e.getMessage());
//            return null;
//        }
//    }

    /**
     * Logs out a user by invalidating their token
     *
     * @param token The authentication token
     * @return true if logout was successful, false otherwise
     */
    public boolean logout(String token) {
        if (token != null && activeSessions.containsKey(token)) {
            activeSessions.remove(token);
            return true;
        }
        return false;
    }

    // Helper methods
    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    boolean verifyPassword(String password, String storedHash, String storedSalt) {
        String hashedPassword = hashPassword(password, storedSalt);
        return hashedPassword.equals(storedHash);
    }

    // Session information inner class storage
    private static class SessionInfo {

        private int userId;
        private UserRole role;
        private long creationTime;

        public SessionInfo(int userId, UserRole role, long creationTime) {
            this.userId = userId;
            this.role = role;
            this.creationTime = creationTime;
        }

        public int getUserId() {
            return userId;
        }

        public UserRole getRole() {
            return role;
        }

        public long getCreationTime() {
            return creationTime;
        }
    }
}
