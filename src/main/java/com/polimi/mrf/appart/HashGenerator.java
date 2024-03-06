package com.polimi.mrf.appart;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    public static String generateMD5(String message) throws UnsupportedEncodingException {
        try {
            return hashString(message, "MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateSHA1(String message) throws UnsupportedEncodingException {
        try {
            return hashString(message, "SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public static String generateSHA256(String message) throws UnsupportedEncodingException {
        try {
            return hashString(message, "SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }



    private static String hashString(String message, String algorithm)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {

            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}
