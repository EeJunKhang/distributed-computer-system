package RMI;

import java.rmi.*;
import java.util.List;

import model.User;
import model.Customer;
import model.Admin;
import model.AuthToken;

public interface UserInterface extends Remote {

    /**
     * Gets a user by their ID
     * @param token Authentication token
     * @param userId The user ID
     * @return The user object or null if not found or unauthorized
     * @throws RemoteException If a remote error occurs
     */
    User getUserById(AuthToken token, int userId) throws RemoteException;
    
    /**
     * Gets a user by their username
     * @param token Authentication token
     * @param username The username
     * @return The user object or null if not found or unauthorized
     * @throws RemoteException If a remote error occurs
     */
    User getUserByUsername(AuthToken token, String username) throws RemoteException;
    
    /**
     * Gets all users in the system (admin only)
     * @param token Authentication token
     * @return List of all users or null if unauthorized
     * @throws RemoteException If a remote error occurs
     */
    List<User> getAllUsers(AuthToken token) throws RemoteException;
    
    /**
     * Gets all customers in the system (admin only)
     * @param token Authentication token
     * @return List of all customers or null if unauthorized
     * @throws RemoteException If a remote error occurs
     */
    List<Customer> getAllCustomers(AuthToken token) throws RemoteException;
    
    /**
     * Gets all admins in the system (admin only)
     * @param token Authentication token
     * @return List of all admins or null if unauthorized
     * @throws RemoteException If a remote error occurs
     */
    List<Admin> getAllAdmins(AuthToken token) throws RemoteException;
    
    /**
     * Updates a user's profile information
     * @param token Authentication token
     * @param userId ID of the user to update
     * @param firstName New first name (or null to keep existing)
     * @param lastName New last name (or null to keep existing)
     * @param email New email (or null to keep existing)
     * @param address New address (or null to keep existing)
     * @param contactNumber New contact number (or null to keep existing)
     * @return true if successful, false if failed or unauthorized
     * @throws RemoteException If a remote error occurs
     */
    boolean updateUserProfile(AuthToken token, int userId, String firstName, 
                            String lastName, String email, String address, 
                            String contactNumber) throws RemoteException;
    
    /**
     * Changes a user's password
     * @param token Authentication token
     * @param userId ID of the user to update
     * @param currentPassword Current password (required for validation)
     * @param newPassword New password
     * @return true if successful, false if failed or unauthorized
     * @throws RemoteException If a remote error occurs
     */
    boolean changePassword(AuthToken token, int userId, String currentPassword, 
                            String newPassword) throws RemoteException;
    
    /**
     * Deletes a user account (admin only)
     * @param token Authentication token
     * @param userId ID of the user to delete
     * @return true if successful, false if failed or unauthorized
     * @throws RemoteException If a remote error occurs
     */
    boolean deleteUser(AuthToken token, int userId) throws RemoteException;
    
    /**
     * Promotes a customer to admin status (admin only)
     * @param token Authentication token
     * @param userId ID of the user to promote
     * @return true if successful, false if failed or unauthorized
     * @throws RemoteException If a remote error occurs
     */
    boolean promoteToAdmin(AuthToken token, int userId) throws RemoteException;
}