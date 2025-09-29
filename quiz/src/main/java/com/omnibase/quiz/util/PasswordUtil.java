package com.omnibase.quiz.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class for generating password hashes.
 * This can be used to generate BCrypt hashes for manual database insertion.
 */
public class PasswordUtil {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    public static void main(String[] args) {
        // Generate password hashes for common admin passwords
        System.out.println("Password hashes for admin users:");
        System.out.println("'secret' -> " + encoder.encode("secret"));
        System.out.println("'admin123' -> " + encoder.encode("admin123"));
        System.out.println("'password' -> " + encoder.encode("password"));
        System.out.println("'admin' -> " + encoder.encode("admin"));
        
        // You can add more passwords here as needed
        if (args.length > 0) {
            for (String password : args) {
                System.out.println("'" + password + "' -> " + encoder.encode(password));
            }
        }
    }
    
    /**
     * Generate a BCrypt hash for a given password
     * @param password The plain text password
     * @return The BCrypt hash
     */
    public static String hashPassword(String password) {
        return encoder.encode(password);
    }
    
    /**
     * Verify if a plain text password matches a BCrypt hash
     * @param password The plain text password
     * @param hash The BCrypt hash
     * @return true if they match, false otherwise
     */
    public static boolean verifyPassword(String password, String hash) {
        return encoder.matches(password, hash);
    }
}