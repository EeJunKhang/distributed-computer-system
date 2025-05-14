package manager.testclient;

import enums.UserRole;
import rmi.AuthInterface;
import utils.TokenStorage;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import model.LoginCredential;
import model.RegisterCredential;
import model.AuthToken;
import model.User;
import security.RMISSLClientSocketFactory;
import utils.ConfigReader;

public class AuthClient {
    
    private static AuthInterface authService;
    private static AuthToken authToken = null;
    
    public static void main(String[] args) {
        try {
            connectToServer();
            
            authToken = TokenStorage.loadToken();
            if (authToken != null) {
                System.out.println("Found saved authentication session");
                if (verifyLoadedToken()) {
                    System.out.println("Session restored successfully!");
                } else {
                    System.out.println("Saved session has expired. Please login again.");
                    authToken = null;
                    TokenStorage.deleteToken();
                }
            }
            
            runConsoleMenu();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
//    private static void connectToServer() throws RemoteException, NotBoundException {
//        String serverIP = ConfigReader.getServerIP();
//        int rmiPort = ConfigReader.getRmiPort();
//        
//        System.out.println("Connecting to server at " + serverIP + ":" + rmiPort);
//        
//        Registry registry = LocateRegistry.getRegistry(serverIP, rmiPort);
//        authService = (AuthInterface) registry.lookup("AuthService");
//        
//        System.out.println("Connected to authentication service");
//    }
    
    private static void connectToServer() throws RemoteException, NotBoundException {
        String serverIP = ConfigReader.getServerIP();
        int rmiPort = ConfigReader.getRmiPort();

        System.out.println("Connecting to server at " + serverIP + ":" + rmiPort);

        Registry registry = LocateRegistry.getRegistry(serverIP, rmiPort, new RMISSLClientSocketFactory());
        authService = (AuthInterface) registry.lookup("AuthService");

        System.out.println("Connected to authentication service");
    }

    
    private static boolean verifyLoadedToken() {
        try {
            if (authToken == null) {
                return false;
            }
            
            boolean isValid = authService.verifyToken(authToken);
            if (isValid) {
                // fetch user info here
                // == if needed
                User user = authService.getUserByToken(authToken);
                UserRole userrole = authService.getUserRoleByToken(authToken);
                if (user != null) {
                    System.out.println("Welcome back, " + userrole + " "+ user.getUsername() + "!");
                }       
            }
            return isValid;
        } catch (RemoteException e) {
            System.err.println("Error verifying token: " + e.getMessage());
            return false;
        }
    }
    
    private static void runConsoleMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n=== Authentication Client ===");
            if (authToken != null) {
                System.out.println("Status: Logged In");
            } else {
                System.out.println("Status: Not Logged In");
            }
            
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Verify Token");
            System.out.println("4. Logout");
            System.out.println("5. Show User Profile");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine();
            
            try {
                switch (choice) {
                    case "1" -> handleLogin(scanner);
                    case "2" -> handleRegister(scanner);
                    case "3" -> handleVerifyToken();
                    case "4" -> handleLogout();
                    case "5" -> showUserProfile();
                    case "6" -> exit = true;
                    default -> System.out.println("Invalid option!");
                }
            } catch (RemoteException e) {
                System.err.println("Error communicating with server: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
    
    private static void handleLogin(Scanner scanner) throws RemoteException {
        if (authToken != null) {
            System.out.println("You are already logged in. Please logout first.");
            return;
        }
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        LoginCredential credential = new LoginCredential(username, password);
        
        AuthToken token = authService.handleLogin(credential);
        
        if (token != null) {
            authToken = token;
            System.out.println("Login successful! Token: " + token.getToken());
            
            if (TokenStorage.saveToken(authToken)) {
                System.out.println("Session saved for future use.");
            } else {
                System.out.println("Warning: Failed to save session.");
            }
        } else {
            System.out.println("Login failed. Invalid credentials.");
        }
    }
    
    private static void handleRegister(Scanner scanner) throws RemoteException {
        if (authToken != null) {
            System.out.println("You are already logged in. Please logout first.");
            return;
        }
        
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        
        System.out.print("Enter contact number: ");
        String contactNumber = scanner.nextLine();
        
        @SuppressWarnings("unused")
        RegisterCredential credential = new RegisterCredential(
            firstName, lastName, username, password, email, address, contactNumber
        );
        
//        AuthToken token = authService.handleRegister(credential);
//        
//        if (token != null) {
//            authToken = token;
//            System.out.println("Registration successful! Token: " + token.getToken());
//            
//            if (TokenStorage.saveToken(authToken)) {
//                System.out.println("Session saved for future use.");
//            } else {
//                System.out.println("Warning: Failed to save session.");
//            }
//        } else {
//            System.out.println("Registration failed. Username may already exist.");
//        }
    }
    
    private static void handleVerifyToken() throws RemoteException {
        if (authToken == null) {
            System.out.println("You're not logged in!");
            return;
        }
        
        boolean isValid = authService.verifyToken(authToken);
        
        if (isValid) {
            System.out.println("Your session is valid!");
        } else {
            System.out.println("Your session has expired. Please login again.");
            authToken = null;
            TokenStorage.deleteToken();
        }
    }
    
    private static void handleLogout() throws RemoteException {
        if (authToken == null) {
            System.out.println("You're not logged in!");
            return;
        }
        
        boolean success = authService.handleLogout(authToken);
        
        if (success) {
            System.out.println("Logout successful!");
            authToken = null;
            
            if (TokenStorage.deleteToken()) {
                System.out.println("Saved session removed.");
            } else {
                System.out.println("Warning: Failed to remove saved session.");
            }
        } else {
            System.out.println("Logout failed.");
        }
    }
    
    private static void showUserProfile() throws RemoteException {
        if (authToken == null) {
            System.out.println("You're not logged in!");
            return;
        }
        
        User user = authService.getUserByToken(authToken);
        
        if (user != null) {
            System.out.println("\n=== User Profile ===");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Name: " + user.getFirstName() + " " + user.getLastName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Role: " + user.getRole());
        } else {
            System.out.println("Failed to retrieve user profile. Your session may have expired.");
            authToken = null;
            TokenStorage.deleteToken();
        }
    }
}