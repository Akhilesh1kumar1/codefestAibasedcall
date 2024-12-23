package com.sr.capital.config;

import com.omunify.encryption.algorithm.AES256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class FieldEncryptionService {

    @Autowired
    private AES256 aes256;

    public void encryptFields(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(EncryptField.class)) {
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(object);
                    if (value != null) {
                        String encryptedValue = aes256.encrypt(value.toString());
                        field.set(object, encryptedValue);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void decryptFields(Object object) throws Exception {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(EncryptField.class)) {
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null) {
                    String decryptedValue = aes256.decrypt(value.toString());
                    field.set(object, decryptedValue);
                }
            }
        }
    }
}
