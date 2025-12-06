package com.lms.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password utility for generating BCrypt password hashes (helper for DB migration/testing)
 */
public class PasswordUtil {
    /**
     * Hash a plain password using BCrypt.
     * @param plain plain-text password
     * @return BCrypt hash
     */
    public static String hash(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt());
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java com.lms.util.PasswordUtil <password>");
            System.exit(1);
        }
        String plain = args[0];
        String hash = hash(plain);
        System.out.println(hash);
    }
}
