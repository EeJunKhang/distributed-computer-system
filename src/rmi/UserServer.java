package rmi;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import manager.AuthManager;
import manager.UserManager;
import model.User;
import model.Customer;
import model.Admin;
import model.AuthToken;
import utils.IPIdentifier;

public class UserServer extends UnicastRemoteObject implements UserInterface {

    private final UserManager userManager;
    private final AuthManager authManager;

    public UserServer() throws RemoteException {
        super();
        this.userManager = UserManager.getInstance();
        this.authManager = AuthManager.getInstance();
    }

    private void showClientIP() {
        String clientIP = IPIdentifier.getClientIP();
        System.out.println("User Service called from IP: " + clientIP);
    }

    private User validateTokenAndGetUser(AuthToken token) {
        if (token == null) {
            return null;
        }
        return authManager.getUserByToken(token.getToken());
    }

    @Override
    public User getUserById(AuthToken token, int userId) throws RemoteException {
        showClientIP();

        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for getUserById from " + IPIdentifier.getClientIP());
            return null;
        }

        return userManager.getUserById(userId);
    }

    @Override
    public User getUserByUsername(AuthToken token, String username) throws RemoteException {
        showClientIP();

        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for getUserByUsername from " + IPIdentifier.getClientIP());
            return null;
        }

        return userManager.getUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers(AuthToken token) throws RemoteException {
        showClientIP();

        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for getAllUsers from " + IPIdentifier.getClientIP());
            return null;
        }

        return userManager.getAllUsers();
    }

    @Override
    public List<Customer> getAllCustomers(AuthToken token) throws RemoteException {
        showClientIP();

        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for getAllCustomers from " + IPIdentifier.getClientIP());
            return null;
        }

        return userManager.getAllCustomers();
    }

    @Override
    public List<Admin> getAllAdmins(AuthToken token) throws RemoteException {
        showClientIP();

        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for getAllAdmins from " + IPIdentifier.getClientIP());
            return null;
        }

        return userManager.getAllAdmins();
    }

    @Override
    public boolean updateUserProfile(AuthToken token, int userId, String firstName,
                                        String lastName, String email, String address,
                                        String contactNumber) throws RemoteException {
        showClientIP();

        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for updateUserProfile from " + IPIdentifier.getClientIP());
            return false;
        }

        return userManager.updateUserProfile(token.getToken(), userId, firstName,
                                                lastName, email, address, contactNumber);
    }

    @Override
    public boolean changePassword(AuthToken token, int userId, String currentPassword,
                                    String newPassword) throws RemoteException {
        showClientIP();

        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for changePassword from " + IPIdentifier.getClientIP());
            return false;
        }

        return userManager.changePassword(token.getToken(), userId, currentPassword, newPassword);
    }

    @Override
    public boolean deleteUser(AuthToken token, int userId) throws RemoteException {
        showClientIP();

        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for deleteUser from " + IPIdentifier.getClientIP());
            return false;
        }

        return userManager.deleteUser(token.getToken(), userId);
    }

    @Override
    public boolean promoteToAdmin(AuthToken token, int userId) throws RemoteException {
        showClientIP();

        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for promoteToAdmin from " + IPIdentifier.getClientIP());
            return false;
        }

        return userManager.promoteToAdmin(token.getToken(), userId);
    }
}