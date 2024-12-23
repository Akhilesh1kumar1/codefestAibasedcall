package com.sr.capital.external.truthscreen.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;




public class TruthScreenUtility {

    private static final String CIPHER_NAME = "AES/CBC/PKCS5PADDING";
    private static final int CIPHER_KEY_LEN = 16;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String encrypt(String key, String iv, Object data) {
        try {
            byte[] sha = generateSha512Hash(key.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : sha) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            String shaKey = sb.substring(0, 16);

            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
            SecretKeySpec secretKey = new SecretKeySpec(fixKey(shaKey).getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            String jsonData = objectMapper.writeValueAsString(data);
            byte[] encryptedData = cipher.doFinal(jsonData.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData) + ":" + Base64.getEncoder().encodeToString(iv.getBytes("UTF-8"));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T decrypt(String key, String data, Class<T> clazz) {
        try {
            if (data == null) {
                throw new IllegalArgumentException("Invalid encrypted data format. Expected 'encryptedData:IV'.");
            }
            byte[] sha = generateSha512Hash(key.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : sha) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            String shaKey = sb.substring(0, 16);

            String[] parts = data.split(":");
            IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(parts[1]));
            SecretKeySpec secretKey = new SecretKeySpec(shaKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(parts[0]));
            String jsonData = new String(original);
            return objectMapper.readValue(jsonData, clazz);
        } catch (Exception ex) {
            throw new RuntimeException("Decryption failed: " + ex.getMessage(), ex);
        }
    }

    private static String fixKey(String key) {
        if (key.length() < CIPHER_KEY_LEN) {
            key = String.format("%-16s", key).replace(' ', '0');
        }
        return key.length() > CIPHER_KEY_LEN ? key.substring(0, CIPHER_KEY_LEN) : key;
    }

    public static String getIV() {
        String ivChars = "1234567890";
        StringBuilder iv = new StringBuilder();
        Random rnd = new Random();
        while (iv.length() < 16) {
            int index = (int) (rnd.nextFloat() * ivChars.length());
            iv.append(ivChars.charAt(index));
        }
        return iv.toString();
    }

    private static byte[] generateSha512Hash(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            return digest.digest(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
