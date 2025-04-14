/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import model.LoginCredential;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

/**
 *
 * @author ejunk
 */
public class TokenUtil {

    // Secret key for signing (keep this secure and private!)
    private static final String SECRET_KEY = "YgL6iFU0yGm2E9BjAxlE6hHdo3KzqM8R"; // Use a strong, random key
    private static final long EXPIRATION_TIME = 30 * 24 * 60 * 60 * 1000L; // 30 days in milliseconds
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    // Generate a token for a username
    public static String generateToken(LoginCredential credential) {
        try {
            long nowMillis = System.currentTimeMillis();
            String randomString = generateRandomString(16); // 16 chars of randomness
            String tokenData = credential.getUsername() + ":" + credential.getPassword() + ":" + nowMillis + ":" + randomString;

            // Sign the token data with HMAC-SHA256
            String signature = hmacSha256(tokenData, SECRET_KEY);

            // Combine data and signature, then encode with Base64
            String token = tokenData + ":" + signature;
            return Base64.getUrlEncoder().withoutPadding().encodeToString(token.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    // Validate a token and return the username if valid
    public static String validateToken(String encodedToken) {
        try {
            // Decode Base64 token
            String token = new String(Base64.getUrlDecoder().decode(encodedToken), StandardCharsets.UTF_8);
            String[] parts = token.split(":");
            if (parts.length != 5) {
                return null; // Invalid format
            }

            String username = parts[0];
            String password = parts[1];

            long issuedAt = Long.parseLong(parts[2]);
            String randomString = parts[3];
            String signature = parts[4];

            // Reconstruct the data and verify the signature
            String tokenData = username + ":" + password + ":" + issuedAt + ":" + randomString;
            String expectedSignature = hmacSha256(tokenData, SECRET_KEY);

            // Check signature and expiration
            if (!signature.equals(expectedSignature)) {
                return null; // Tampered token
            }
            if (System.currentTimeMillis() > issuedAt + EXPIRATION_TIME) {
                return null; // Expired token
            }

            return username; // Valid token, return username
        } catch (Exception e) {
            return null; // Invalid token
        }
    }

    // Compute HMAC-SHA256 signature
    private static String hmacSha256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    // Generate a random string for token uniqueness
    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
