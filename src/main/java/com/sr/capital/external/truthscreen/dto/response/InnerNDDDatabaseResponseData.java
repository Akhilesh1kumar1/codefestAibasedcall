package com.sr.capital.external.truthscreen.dto.response;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.config.EncryptField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InnerNDDDatabaseResponseData {

    private List<Map<String,Object>> data;
    private Map<String,Object> user_define_weight;

    public static InnerNDDDatabaseResponseData encrypt(InnerNDDDatabaseResponseData config, AES256 aes256) {
        List<Map<String, Object>> encryptedData = encryptDataDetails(config.getData(), aes256);
        Map<String, Object> encryptedUserDefineWeight = encryptUserDefinceWeight(config.getUser_define_weight(), aes256); // Pass aes256 here

        InnerNDDDatabaseResponseData encryptedConfig = new InnerNDDDatabaseResponseData();
        encryptedConfig.setData(encryptedData);
        encryptedConfig.setUser_define_weight(encryptedUserDefineWeight);

        return encryptedConfig; // Return a new encrypted object
    }

    private static List<Map<String, Object>> encryptDataDetails(List<Map<String, Object>> data, AES256 aes256) {
        if (data == null) {
            return null; // Handle null input
        }
        return data.stream()
                .map(map -> encryptMap(map, aes256))
                .collect(Collectors.toList());
    }

    private static Map<String, Object> encryptMap(Map<String, Object> map, AES256 aes256) {
        if (map == null) {
            return null;
        }
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> aes256.encrypt(e.getValue().toString())
                ));
    }

    private static Map<String, Object> encryptUserDefinceWeight(Map<String, Object> userDefineWeight, AES256 aes256) {
        if (userDefineWeight == null) {
            return null;
        }
        return userDefineWeight.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> aes256.encrypt(e.getValue().toString())
                ));
    }

    public static InnerNDDDatabaseResponseData decrypt(InnerNDDDatabaseResponseData config, AES256 aes256) {
        List<Map<String, Object>> decryptedData = decryptDataDetails(config.getData(), aes256);
        Map<String, Object> decryptedUserDefineWeight = decryptUserDefinceWeight(config.getUser_define_weight(), aes256);

        InnerNDDDatabaseResponseData decryptedConfig = new InnerNDDDatabaseResponseData();
        decryptedConfig.setData(decryptedData);
        decryptedConfig.setUser_define_weight(decryptedUserDefineWeight);

        return decryptedConfig;
    }

    private static List<Map<String, Object>> decryptDataDetails(List<Map<String, Object>> data, AES256 aes256) {
        if (data == null) {
            return null;
        }
        return data.stream()
                .map(map -> decryptMap(map, aes256))
                .collect(Collectors.toList());
    }

    private static Map<String, Object> decryptMap(Map<String, Object> map, AES256 aes256) {
        if (map == null) {
            return null;
        }
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            String decryptedValue = aes256.decrypt(e.getValue().toString());
                            try {
                                // Attempt to parse as a number (Integer, Double, etc.)
                                return Integer.parseInt(decryptedValue); // Or Double.parseDouble(), etc.
                            } catch (NumberFormatException ex) {
                                return decryptedValue; // If not a number, return as String
                            }
                        }
                ));
    }

    private static Map<String, Object> decryptUserDefinceWeight(Map<String, Object> userDefineWeight, AES256 aes256) {
        if (userDefineWeight == null) {
            return null;
        }
        return userDefineWeight.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            String decryptedValue = aes256.decrypt(e.getValue().toString());
                            try {
                                // Attempt to parse as a number (Integer, Double, etc.)
                                return Integer.parseInt(decryptedValue); // Or Double.parseDouble(), etc.
                            } catch (NumberFormatException ex) {
                                return decryptedValue; // If not a number, return as String
                            }
                        }
                ));
    }

}
