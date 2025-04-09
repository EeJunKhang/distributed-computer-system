package RMI;

import model._LoginCredential;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import model._RegisterCredential;
import model.User;
import model.AuthToken;
import utils.IPIdentifier;
import manager.AuthManager;

public class AuthServer extends UnicastRemoteObject implements AuthInterface {
    
    private final AuthManager authManager;
    
    public AuthServer() throws RemoteException {
        super();
        this.authManager = AuthManager.getInstance();
    }
    
    private void showClientIP() {
        String clientIP = IPIdentifier.getClientIP();
        System.out.println("Client Call from IP: " + clientIP);
    }

    @Override
    public AuthToken handleLogin(_LoginCredential credential) throws RemoteException {
        showClientIP();
        System.out.println("Login attempt: " + credential.getUsername());
        
        String tokenStr = authManager.authenticateUser(credential.getUsername(), credential.getPassword());
        return (tokenStr != null) ? new AuthToken(tokenStr) : null;
    }

    @Override
    public boolean handleLogout(AuthToken token) throws RemoteException {
        showClientIP();
        if (token == null) {
            System.out.println("Logout attempt with null token");
            return false;
        }
        
        System.out.println("Logout attempt with token: " + token.getToken());
        return authManager.logout(token.getToken());
    }

    @Override
    public boolean verifyToken(AuthToken token) throws RemoteException {
        showClientIP();
        if (token == null) {
            return false;
        }
        return authManager.validateToken(token.getToken());
    }

    @Override
    public AuthToken handleRegister(_RegisterCredential credential) throws RemoteException {
        showClientIP();
        
        boolean registered = authManager.registerUser(
            credential.getFirstName(),
            credential.getLastName(),
            credential.getUsername(),
            credential.getPassword(),
            credential.getEmail(),
            credential.getAddress(),
            credential.getContactNumber(),
            false  // Not an admin by default
        );
        
        if (registered) {
            // immediately log them in
            String tokenStr = authManager.authenticateUser(credential.getUsername(), credential.getPassword());
            return (tokenStr != null) ? new AuthToken(tokenStr) : null;
        }
        
        return null;
    }
    
    @Override
    public User getUserByToken(AuthToken token) throws RemoteException {
        showClientIP();
        if (token == null) {
            return null;
        }
        return authManager.getUserByToken(token.getToken());
    }
}