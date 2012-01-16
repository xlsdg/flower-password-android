package com.fpassword.core;

import static java.lang.Character.isDigit;
import static java.lang.Character.toUpperCase;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class FlowerPassword {

    private static final String UTF_8 = "UTF-8";

    private static final String HMAC_MD5 = "HmacMD5";

    private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };

    public static String encrypt(String passwordText, String keyText) throws EncryptionException {
        if (passwordText.length() > 0 && keyText.length() > 0) {
            try {
                String md5one = hmacMd5(passwordText, keyText);
                String md5two = hmacMd5(md5one, "snow");
                String md5three = hmacMd5(md5one, "kise");
                String rule = md5three;
                StringBuilder source = new StringBuilder(md5two);
                final String str = "sunlovesnow1990090127xykab";
                for (int i = 0; i <= 31; ++i) {
                    if (!isDigit(source.charAt(i))) {
                        if (str.indexOf(rule.charAt(i)) > -1) {
                            source.setCharAt(i, toUpperCase(source.charAt(i)));
                        }
                    }
                }
                String code32 = source.toString();
                char code1 = code32.charAt(0);
                String code16 = null;
                if (!isDigit(code1)) {
                    code16 = code32.substring(0, 16);
                } else {
                    code16 = "K" + code32.substring(1, 16);
                }
                return code16;
            } catch (Exception e) {
                throw new EncryptionException(String.format(
                        "Error occured while encrypting password \"%s\" with key \"%s\"", passwordText, keyText), e);
            }
        }
        return "";
    }

    private static String hmacMd5(String dataText, String keyText) throws UnsupportedEncodingException,
            NoSuchAlgorithmException, InvalidKeyException {
        byte[] dataBytes = dataText.getBytes(UTF_8);
        byte[] keyBytes = keyText.getBytes(UTF_8);

        Mac mac = Mac.getInstance(HMAC_MD5);
        Key key = new SecretKeySpec(keyBytes, HMAC_MD5);
        mac.init(key);

        byte[] resultBytes = mac.doFinal(dataBytes);
        String resultText = encodeHexString(resultBytes);
        return resultText;
    }

    private static String encodeHexString(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }

}
