package com.sr.capital.external.truthscreen.service.impl;

import com.sr.capital.external.truthscreen.dto.request.EncryptRequestDto;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;


@Service
public class EncryptionService {

    private static final String CIPHER_NAME = "AES/CBC/PKCS5PADDING";
    private static final int CIPHER_KEY_LEN = 16;

    public String encrypt(EncryptRequestDto request) {
        try {
            String key = generateKey("your-password");
            String iv = generateIV();
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
            SecretKeySpec secretKey = new SecretKeySpec(fixKey(key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedData = cipher.doFinal(request.getData().getBytes("UTF-8"));
            String encryptedDataInBase64 = Base64.getEncoder().encodeToString(encryptedData);
            String ivInBase64 = Base64.getEncoder().encodeToString(iv.getBytes("UTF-8"));

            return encryptedDataInBase64 + ":" + ivInBase64;
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    public String decrypt(EncryptRequestDto request) {
        try {
            String key = generateKey("your-password");
            String[] parts = request.getData().split(":");
            IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(parts[1]));
            SecretKeySpec secretKey = new SecretKeySpec(fixKey(key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] decodedEncryptedData = Base64.getDecoder().decode(parts[0]);
            byte[] original = cipher.doFinal(decodedEncryptedData);

            return new String(original);
        } catch (Exception e) {
            throw new RuntimeException("Decryption error", e);
        }
    }

    private String generateKey(String password) throws Exception {
        byte[] sha = MessageDigest.getInstance("SHA-512").digest(password.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : sha) {
            sb.append(String.format("%02x", b));
        }
        return sb.substring(0, CIPHER_KEY_LEN);
    }

    private String generateIV() {
        String ivChars = "1234567890";
        StringBuilder iv = new StringBuilder();
        Random rnd = new Random();
        while (iv.length() < CIPHER_KEY_LEN) {
            int index = (int) (rnd.nextFloat() * ivChars.length());
            iv.append(ivChars.charAt(index));
        }
        return iv.toString();
    }

    private String fixKey(String key) {
        if (key.length() < CIPHER_KEY_LEN) {
            int numPad = CIPHER_KEY_LEN - key.length();
            for (int i = 0; i < numPad; i++) {
                key += "0";
            }
        } else if (key.length() > CIPHER_KEY_LEN) {
            return key.substring(0, CIPHER_KEY_LEN);
        }
        return key;
    }
}
