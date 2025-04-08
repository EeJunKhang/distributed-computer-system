package utils;

import java.io.*;
import model.AuthToken;

public class TokenStorage {
    private static final String TOKEN_FILE_PATH = "auth_token.ser";
    
    /**
     * Saves the authentication token to a file
     * @param token The token to save
     * @return true if saved successfully, false otherwise
     */
    public static boolean saveToken(AuthToken token) {
        if (token == null) {
            return false;
        }
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TOKEN_FILE_PATH))) {
            out.writeObject(token);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving token: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Loads the authentication token from file
     * @return The token if loaded successfully, null otherwise
     */
    public static AuthToken loadToken() {
        File tokenFile = new File(TOKEN_FILE_PATH);
        if (!tokenFile.exists()) {
            return null;
        }
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(TOKEN_FILE_PATH))) {
            return (AuthToken) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading token: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Deletes the saved token file
     * @return true if deleted successfully, false otherwise
     */
    public static boolean deleteToken() {
        File tokenFile = new File(TOKEN_FILE_PATH);
        if (!tokenFile.exists()) {
            return true; // File doesn't exist, so technically it's already deleted
        }
        
        return tokenFile.delete();
    }
}