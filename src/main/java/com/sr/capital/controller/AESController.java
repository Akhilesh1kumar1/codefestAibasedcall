package com.sr.capital.controller;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.config.AppProperties;
import com.sr.capital.util.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aes")
@RequiredArgsConstructor
public class AESController {

    private final AppProperties appProperties;

    @PostMapping("/encrypt")
    public ResponseEntity<String> encryptPayload(String payload,
                                                 String key) {
        try {
            String encryptedData = AESUtil.encrypt(payload, appProperties.getAesSecretKey(), key, appProperties.getAesIVKey());
            return ResponseEntity.ok(encryptedData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during encryption: " + e.getMessage());
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decryptPayload(String payload,
                                                 String key) {
        try {
            String decryptedData = AESUtil.decrypt(payload.replace("@", "/"), appProperties.getAesSecretKey(), key, appProperties.getAesIVKey());

            return ResponseEntity.ok(decryptedData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during decryption: " + e.getMessage());
        }
    }
}
   
