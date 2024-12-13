package com.sr.capital.util;

import com.sr.capital.CommonConstant;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESUtil {

    /**
     * Encrypts a plaintext string using the given AES key.
     *
     * @param plainText The plaintext string to encrypt.
     * @param key       The Base64-encoded AES key.
     * @return The encrypted string in Base64 encoding.
     * @throws Exception If encryption fails.
     */
    public static String encrypt(String plainText, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), CommonConstant.ALGORITHM);
        Cipher cipher = Cipher.getInstance(CommonConstant.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts a Base64-encoded ciphertext string using the given AES key.
     *
     * @param cipherText The encrypted string in Base64 encoding.
     * @param dynamicIv
     * @param aesIVKey
     * @return The decrypted plaintext string.
     */
    public static String decrypt(String cipherText, String secretKey, String dynamicIv, String aesIVKey) {
        // Decode the Base64 encoded cipherText
        byte[] cipherBytes = Base64.getDecoder().decode(cipherText);

        // Create a SecretKeySpec from the given secret key (Base64 decoded)
        SecretKey secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "AES");

        // Initialize the cipher in DECRYPT_MODE with the secret key
        Cipher cipher = null;
        byte[] decryptedBytes;
        try {
            // Example IV (you should use the same IV used during encryption)
            IvParameterSpec ivParameterSpec = new IvParameterSpec((Base64Util.decode(aesIVKey) + dynamicIv).getBytes(StandardCharsets.UTF_8));

            cipher = Cipher.getInstance(CommonConstant.AES_CBC_PADDING); // AES CBC with PKCS5 padding
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            decryptedBytes = cipher.doFinal(cipherBytes);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

        // Convert decrypted bytes back to a string
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static String getIvValue(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Step 1: Extract 'request-id' from headers for validation
        String requestId = httpRequest.getHeader("request-id");

        if (requestId == null) {
            // If 'request-id' is not present, return an error response
            try {
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing 'request-id' header");
                return null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return requestId.substring(requestId.length() - 4);
        }

    }

    public static String removeExtraQuotes(String input) {
        int start = 0;
        int end = 0;
        if (input != null && input.length() >= 2) {
            end = input.length();
            if (input.charAt(0) == '\"') {
                start = 1;
            }
            if (input.charAt(input.length() -1) == '\"') {
                end = input.length() - 1;
            }
            input = input.substring(start, end);
        }
        return input;
    }
}
