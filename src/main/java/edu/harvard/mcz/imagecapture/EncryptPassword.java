package edu.harvard.mcz.imagecapture;

import org.jasypt.util.text.AES256TextEncryptor;

import java.util.Scanner;

public class EncryptPassword {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the password you want to encrypt");
        String passwordToEncrypt = in.nextLine();
        System.out.println("Please enter the password for the encryption");
        String encryptPassword = in.nextLine();
        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(encryptPassword);
        System.out.println(textEncryptor.encrypt(passwordToEncrypt));
    }
}
