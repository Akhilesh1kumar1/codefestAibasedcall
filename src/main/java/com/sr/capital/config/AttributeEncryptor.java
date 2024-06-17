package com.sr.capital.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;



//@Component
//@Converter(autoApply = true)
public class AttributeEncryptor {//implements AttributeConverter<String, String> {

    private AppProperties appProperties;
    private static final String AES = "AES";
    private final Key key;
    private final Cipher cipher;

    public AttributeEncryptor(AppProperties appProperties) throws Exception {
        key = new SecretKeySpec(appProperties.getAesEncryptionKey().getBytes(), AES);
        cipher = Cipher.getInstance(AES);
    }

    //@Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            if(StringUtils.hasText(attribute)) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
            }
            return null;
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    //@Override
    public String convertToEntityAttribute(String dbData) {
        try {
            if(StringUtils.hasText(dbData)) {
                cipher.init(Cipher.DECRYPT_MODE, key);
                return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
            }
            return null;
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalStateException(e);
        }
    }
}
