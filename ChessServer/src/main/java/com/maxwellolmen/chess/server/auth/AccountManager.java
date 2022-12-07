package com.maxwellolmen.chess.server.auth;

import com.maxwellolmen.chess.server.Main;
import com.maxwellolmen.chess.server.sql.SQLManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;

public final class AccountManager {

    private final String symbols = "ABCDEFGJKLMNPRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public void signup(String username, String password) throws SQLException {
        String salt = generateSalt();
        String saltedPassword = salt + password;
        String hashedPassword = generateHash(saltedPassword);

        Main.sqlManager.saveHash(username, hashedPassword, salt);

        System.out.println("Hashed password for user " + username + ": " + hashedPassword);
    }

    public int login(String username, String password) {
        String salt;

        try {
            salt = Main.sqlManager.loadSalt(username);
        } catch (SQLException e) {
            return 0;
        }

        if (salt == null) {
            return 0;
        }

        String saltedPassword = salt + password;
        String hashedPassword = generateHash(saltedPassword);

        String storedHash = null;
        try {
            storedHash = Main.sqlManager.loadHash(username);
        } catch (SQLException e) {
            return 0;
        }

        if (storedHash == null) return 0;

        if (hashedPassword.equals(storedHash)) {
            return 2;
        } else {
            return 1;
        }
    }

    public String generateSalt() {
        char[] salt = new char[16];
        SecureRandom r = new SecureRandom();

        for (int i = 0; i < 16; i++) {
            salt[i] = symbols.charAt(r.nextInt(symbols.length()));
        }

        return new String(salt);
    }

    public String generateHash(String password) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = sha.digest(password.getBytes());
            char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash.toString();
    }
}