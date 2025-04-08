package database.test;

import database.UserDAO;
import enums.UserRole;
import model.User;
import model.Customer;

import java.time.LocalDateTime;
import java.util.List;

public class UserDAOTest {

    public static void main(String[] args) {
        testUserDAO();
    }

    public static void testUserDAO() {
        UserDAO userDAO = new UserDAO();

        // Test create
        System.out.println("Testing create user:");
        User newUser = new Customer(
            "John", 
            "Doe", 
            "johndoe123", 
            "hashedPassword", 
            "salt123", 
            "johndoe@example.com", 
            "123 Main Street", 
            "0123456789", 
            LocalDateTime.now()
        );

        boolean created = userDAO.create(newUser);
        System.out.println("User created: " + created);
        System.out.println("User ID: " + newUser.getUserId());

        // Test read
        System.out.println("\nTesting read user:");
        User retrievedUser = userDAO.read(newUser.getUserId());
        System.out.println("Retrieved user: " + retrievedUser);

        // Test update
        System.out.println("\nTesting update user:");
        retrievedUser.setAddress("456 New Address");
        retrievedUser.setContactNumber("0987654321");
        boolean updated = userDAO.update(retrievedUser);
        System.out.println("User updated: " + updated);

        // Test get all users
        System.out.println("\nTesting get all users:");
        List<User> allUsers = userDAO.getAll();
        System.out.println("Total users: " + allUsers.size());
        for (User user : allUsers) {
            System.out.println(user);
        }

        // Test delete
        System.out.println("\nTesting delete user:");
        boolean deleted = userDAO.delete(newUser.getUserId());
        System.out.println("User deleted: " + deleted);

        // Verify deletion
        User deletedUser = userDAO.read(newUser.getUserId());
        System.out.println("User after deletion: " + (deletedUser == null ? "Not found" : deletedUser));
    }
}
