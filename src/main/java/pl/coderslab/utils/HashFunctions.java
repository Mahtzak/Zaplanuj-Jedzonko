package pl.coderslab.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.xml.bind.DatatypeConverter;

public class HashFunctions {

    //Available algorithms are: MD2, MD5, SHA-1, SHA-224, SHA-256, SHA-384, SHA-512


    public static String generateHash(String data, String algorithm, byte[] salt) {
        String hashValue = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance( algorithm );
            messageDigest.reset();
            messageDigest.update( salt );
            byte[] hash = messageDigest.digest( data.getBytes() );
            hashValue = DatatypeConverter.printHexBinary( hash ).toLowerCase();
        } catch (Exception e) {

        }
        return hashValue;
    }

    public static byte[] createSalt() {
        byte[] bytes = new byte[8];
        SecureRandom random = new SecureRandom();
        random.nextBytes( bytes );
        return bytes;
    }


}













    /*public static String getHash(byte[] inputBytes, String algorithm) {
        String hashValue = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance( algorithm );
            messageDigest.update( inputBytes );
            byte[] digestedBytes = messageDigest.digest();
            hashValue = DatatypeConverter.printHexBinary( digestedBytes ).toLowerCase();
        } catch (Exception e) {

        }
        return hashValue;
    }*/
