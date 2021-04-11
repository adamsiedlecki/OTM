package pl.adamsiedlecki.OTM.tools;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// https://www.baeldung.com/sha-256-hashing-java
public class Sha256 {

    public String getSha(String content){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedhash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);

    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
