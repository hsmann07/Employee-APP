/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author MANN
 */
public class PWtester {
    public static void main(String[] args) throws NoSuchAlgorithmException
    {
        String password = "simple";
        byte[] salt = PasswordGenerator.getSalt();
        
        System.out.printf("password: %s%n", PasswordGenerator.getSHA512Password(password, salt));
        System.out.printf("password: %s%n", PasswordGenerator.getSHA512Password(password, salt));
    }
}
