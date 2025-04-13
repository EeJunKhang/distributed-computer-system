package manager.testclient;

import rmi.UserInterface;
import utils.TokenStorage;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;
import model.User;
import model.Customer;
import model.Admin;
import model.AuthToken;
import utils.ConfigReader;

public class UserClient {
    
    private static UserInterface userService;
    private static AuthToken authToken = null;
    
    public static void main(String[] args) {
        try {
            connectToServer();
            
            authToken = TokenStorage.loadToken();
            if (authToken != null) {
                System.out.println("Found saved authentication session");
                // Assume the token is valid
                System.out.println("Using existing session token");
            } else {
                System.out.println("No saved session found. Please run the AuthClient first to login.");
            }
            
            runConsoleMenu();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    private static void connectToServer() throws RemoteException, NotBoundException {
        String serverIP = ConfigReader.getServerIP();
        int rmiPort = ConfigReader.getRmiPort();
        
        System.out.println("Connecting to server at " + serverIP + ":" + rmiPort);
        
        Registry registry = LocateRegistry.getRegistry(serverIP, rmiPort);
        userService = (UserInterface) registry.lookup("UserService");
        
        System.out.println("Connected to user service");
    }
    
    private static void runConsoleMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n=== User Management Client ===");
            if (authToken != null) {
                System.out.println("Status: Using Authentication Token");
            } else {
                System.out.println("Status: No Authentication Token Available");
                System.out.println("Please run AuthClient first to login");
            }
            
            System.out.println("1. Get User by ID");
            System.out.println("2. Get User by Username");
            System.out.println("3. Get All Users");
            System.out.println("4. Get All Customers");
            System.out.println("5. Get All Admins");
            System.out.println("6. Update User Profile");
            System.out.println("7. Change Password");
            System.out.println("8. Delete User");
            System.out.println("9. Promote User to Admin");
            System.out.println("10. Exit");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine();
            
            try {
                switch (choice) {
                    case "1" -> getUserById(scanner);
                    case "2" -> getUserByUsername(scanner);
                    case "3" -> getAllUsers();
                    case "4" -> getAllCustomers();
                    case "5" -> getAllAdmins();
                    case "6" -> updateUserProfile(scanner);
                    case "7" -> changePassword(scanner);
                    case "8" -> deleteUser(scanner);
                    case "9" -> promoteToAdmin(scanner);
                    case "10" -> exit = true;
                    default -> System.out.println("Invalid option!");
                }
            } catch (RemoteException e) {
                System.err.println("Error communicating with server: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
    
    private static void getUserById(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }
        
        System.out.print("Enter user ID: ");
        int userId = Integer.parseInt(scanner.nextLine());
        
        User user = userService.getUserById(authToken, userId);
        
        if (user != null) {
            displayUserInfo(user);
        } else {
            System.out.println("User not found or you don't have permission to view this user.");
        }
    }
    
    private static void getUserByUsername(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        User user = userService.getUserByUsername(authToken, username);
        
        if (user != null) {
            displayUserInfo(user);
        } else {
            System.out.println("User not found or you don't have permission to view this user.");
        }
    }
    
    private static void getAllUsers() throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }
        
        List<User> users = userService.getAllUsers(authToken);
        
        if (users != null && !users.isEmpty()) {
            System.out.println("\n=== All Users ===");
            for (User user : users) {
                System.out.println("ID: " + user.getUserId() + " | Username: " + user.getUsername() + 
                                  " | Role: " + user.getRole() + " | Name: " + user.getFirstName() + " " + 
                                  user.getLastName());
            }
            System.out.println("Total users: " + users.size());
        } else {
            System.out.println("No users found or you don't have permission to view all users.");
        }
    }
    
    private static void getAllCustomers() throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }
        
        List<Customer> customers = userService.getAllCustomers(authToken);
        
        if (customers != null && !customers.isEmpty()) {
            System.out.println("\n=== All Customers ===");
            for (Customer customer : customers) {
                System.out.println("ID: " + customer.getUserId() + " | Username: " + customer.getUsername() + 
                                  " | Name: " + customer.getFirstName() + " " + customer.getLastName());
            }
            System.out.println("Total customers: " + customers.size());
        } else {
            System.out.println("No customers found or you don't have permission to view all customers.");
        }
    }
    
    private static void getAllAdmins() throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }
        
        List<Admin> admins = userService.getAllAdmins(authToken);
        
        if (admins != null && !admins.isEmpty()) {
            System.out.println("\n=== All Admins ===");
            for (Admin admin : admins) {
                System.out.println("ID: " + admin.getUserId() + " | Username: " + admin.getUsername() + 
                                  " | Name: " + admin.getFirstName() + " " + admin.getLastName());
            }
            System.out.println("Total admins: " + admins.size());
        } else {
            System.out.println("No admins found or you don't have permission to view all admins.");
        }
    }
    
    private static void updateUserProfile(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }
        
        System.out.print("Enter user ID to update: ");
        int userId = Integer.parseInt(scanner.nextLine());
        
        System.out.println("Leave fields blank to keep existing values.");
        
        System.out.print("Enter new first name (or press Enter to keep existing): ");
        String firstName = scanner.nextLine();
        if (firstName.isEmpty()) firstName = null;
        
        System.out.print("Enter new last name (or press Enter to keep existing): ");
        String lastName = scanner.nextLine();
        if (lastName.isEmpty()) lastName = null;
        
        System.out.print("Enter new email (or press Enter to keep existing): ");
        String email = scanner.nextLine();
        if (email.isEmpty()) email = null;
        
        System.out.print("Enter new address (or press Enter to keep existing): ");
        String address = scanner.nextLine();
        if (address.isEmpty()) address = null;
        
        System.out.print("Enter new contact number (or press Enter to keep existing): ");
        String contactNumber = scanner.nextLine();
        if (contactNumber.isEmpty()) contactNumber = null;
        
        boolean result = userService.updateUserProfile(authToken, userId, firstName, lastName, 
                                                     email, address, contactNumber);
        
        if (result) {
            System.out.println("User profile updated successfully!");
        } else {
            System.out.println("Failed to update user profile. Check permissions and user ID.");
        }
    }
    
    private static void changePassword(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }
        
        System.out.print("Enter user ID: ");
        int userId = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();
        
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        
        boolean result = userService.changePassword(authToken, userId, currentPassword, newPassword);
        
        if (result) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password. Check current password or permissions.");
        }
    }
    
    private static void deleteUser(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }
        
        System.out.print("Enter user ID to delete: ");
        int userId = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Are you sure you want to delete this user? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("yes")) {
            boolean result = userService.deleteUser(authToken, userId);
            
            if (result) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("Failed to delete user. Check permissions and user ID.");
            }
        } else {
            System.out.println("User deletion cancelled.");
        }
    }
    
    private static void promoteToAdmin(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }
        
        System.out.print("Enter user ID to promote to admin: ");
        int userId = Integer.parseInt(scanner.nextLine());
        
        boolean result = userService.promoteToAdmin(authToken, userId);
        
        if (result) {
            System.out.println("User promoted to admin successfully!");
        } else {
            System.out.println("Failed to promote user. Check permissions and user ID.");
        }
    }
    
    private static void displayUserInfo(User user) {
        System.out.println("\n=== User Details ===");
        System.out.println("ID: " + user.getUserId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Name: " + user.getFirstName() + " " + user.getLastName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Address: " + user.getAddress());
        System.out.println("Contact: " + user.getContactNumber());
        System.out.println("Role: " + user.getRole());
        System.out.println("Created: " + user.getCreatedTime());
    }
}