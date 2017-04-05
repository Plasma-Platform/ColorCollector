package com.templatemonster.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Algorithm
{
    public static String md5(String sum) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(sum.getBytes());
            byte[] bytes = messageDigest.digest();

            return bytesToString(bytes);

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String bytesToString(byte[] bytes) {
        HexDump.setWithByteSeparator(false);
        return HexDump.byteArrayToHexString(bytes);
    }
}