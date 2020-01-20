package com.pointsevenfive.esb.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class MessageHelper {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String genUUID() throws NoSuchAlgorithmException {
        MessageDigest salt = MessageDigest.getInstance("SHA-256");
        salt.update(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
        return bytesToHex(salt.digest());
    }

    private static String bytesToHex(byte[] digest) {
        char[] hexChars = new char[digest.length * 2];
        for (int j = 0; j < digest.length; j++) {
            int v = digest[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
