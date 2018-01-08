package com.bjtu.item.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    private static Logger log = LoggerFactory.getLogger(MD5Util.class);

    private static final String ENCODING = "UTF-8";

    private static String password_safe_code = "66666666666666666666";
    private static String payment_safe_code = "F2GlqKfh!hzi2LB@7$m7s^aGw7X06G";

    private MD5Util() {
    }

    public static String digest(String text) {
        if (text == null) {
            return null;
        }
        try {
            return digest(text.getBytes(ENCODING));
        } catch (UnsupportedEncodingException e) {
            if (log.isErrorEnabled()) {
                LoggerUtil.error(log, e);
            }
        }
        return "";
    }

    public static String digestPassword(String text) {
        if (text == null) {
            return null;
        }
        text = text + password_safe_code;
        try {
            return digest(text.getBytes(ENCODING));
        } catch (UnsupportedEncodingException e) {
            if (log.isErrorEnabled()) {
                LoggerUtil.error(log, e);
            }
        }
        return "";
    }

    public static String digest(byte[] bytes) {
        try {
            byte[] digestedBytes = MessageDigest.getInstance("MD5").digest(bytes);
            return bytesToString(digestedBytes);
        } catch (NoSuchAlgorithmException e) {
            if (log.isErrorEnabled()) {
                LoggerUtil.error(log, e);
            }
        }
        return "";
    }

    private static String bytesToString(byte[] digest) {

        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            hexString.append(Integer.toHexString(
                    (digest[i] & 0x000000FF) | 0xFFFFFF00).substring(6));
        }
        return hexString.toString();
    }

    public static byte[] digestToByte(byte[] bytes) throws NoSuchAlgorithmException {
        byte[] digestedBytes = MessageDigest.getInstance("MD5").digest(bytes);
        return digestedBytes;
    }



    public static void main(String[] args){
        String _key = digest(String.format("%s%s", "[{\"phoneId\":490,\"phoneNumber\":\"081385232926\"}]", "68G9A5P1FWOe"));
        System.out.println(_key);
    }

    public static String digestPaymentInfo(String text) {
        if (text == null) {
            return null;
        }
        text = text + payment_safe_code;
        try {
            return digest(text.getBytes(ENCODING));
        } catch (UnsupportedEncodingException e) {
            if (log.isErrorEnabled()) {
                LoggerUtil.error(log, e);
            }
        }
        return "";
    }
}
