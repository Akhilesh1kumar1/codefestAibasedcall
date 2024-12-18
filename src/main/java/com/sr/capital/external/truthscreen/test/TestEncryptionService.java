package com.sr.capital.external.truthscreen.test;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class TestEncryptionService {
    public TestEncryptResponseDto encryptRequest(TestEncryptRequestDto input) throws Exception {
        String password = "India@26088"; // Replace with the one-time password

        // Step 1: Generate encryption key
        String encryptionKey = generateSHA512Hash(password);

        // Step 2: Prepare input structure
        String jsonString = createJsonString(input);

        // Step 3: Encrypt the input
        String encryptedData = encryptAES128(jsonString, encryptionKey);

        // Step 4: Assign string to "requestData"
        String base64EncodedData = Base64.getEncoder().encodeToString(encryptedData.getBytes(StandardCharsets.UTF_8));

        return new TestEncryptResponseDto(base64EncodedData);
    }

    public TestDecryptResponseDto decryptResponse(TestDecryptRequestDto encryptedResponse) throws Exception {
        String password = "India@26088"; // Replace with the one-time password

        // Generate encryption key
        String encryptionKey = generateSHA512Hash(password);

        // Decode base64 and decrypt the response
        String base64EncodedResponse = encryptedResponse.getResponseData();
        String decodedResponse = new String(Base64.getDecoder().decode(base64EncodedResponse), StandardCharsets.UTF_8);
        String decryptedResponse = decryptAES128(decodedResponse, encryptionKey);

        return new TestDecryptResponseDto(decryptedResponse);
    }

    private String generateSHA512Hash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash).substring(0, 16); // Use first 16 bytes as key
    }

    private String createJsonString(TestEncryptRequestDto input) {
        return "{\"transID\": \"" + input.getTransID() + "\", " +
                "\"docType\": " + input.getDocType() + ", " +
                "\"docNumber\": \"" + input.getDocNumber() + "\"}";
    }

    private String encryptAES128(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return new String(encrypted, StandardCharsets.UTF_8);
    }

    private String decryptAES128(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
