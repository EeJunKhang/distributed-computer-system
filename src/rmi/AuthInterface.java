package rmi;

import enums.UserRole;
import java.rmi.*;
import model.User;
import model.LoginCredential;
import model.RegisterCredential;
import model.AuthToken;

public interface AuthInterface extends Remote {

    /**
     * Authenticates a user and returns an authentication token.
     * @param credential The login credentials.
     * @return The authentication token, or null if authentication fails.
     * @throws RemoteException If a remote error occurs.
     */
    AuthToken handleLogin(LoginCredential credential) throws RemoteException;

    /**
     * Logs out a user by invalidating their token.
     * @param token The authentication token to invalidate.
     * @return True if logout was successful, false otherwise.
     * @throws RemoteException If a remote error occurs.
     */
    boolean handleLogout(AuthToken token) throws RemoteException;

    /**
     * Validates if a given token is valid and not expired.
     * @param token The authentication token to verify.
     * @return True if the token is valid, false otherwise.
     * @throws RemoteException If a remote error occurs.
     */
    boolean verifyToken(AuthToken token) throws RemoteException;

    /**
     * Registers a new user.
     * @param credential The registration credentials.
     * @return The authentication token for the newly registered user,
     * or null if registration fails.
     * @throws RemoteException If a remote error occurs.
     */
    AuthToken handleRegister(RegisterCredential credential) throws RemoteException;
    
    /**
     * Retrieves the user information associated with a valid token.
     * @param token The authentication token.
     * @return The User object if the token is valid, null otherwise.
     * @throws RemoteException If a remote error occurs.
     */
    User getUserByToken(AuthToken token) throws RemoteException;
    
    UserRole getUserRoleByToken(AuthToken token) throws RemoteException;
}