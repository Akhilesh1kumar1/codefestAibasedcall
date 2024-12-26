package com.sr.capital.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;

@Component
public class EncryptionEventListener extends AbstractMongoEventListener<Object> {

    @Autowired
    private MongoFieldEncryptionService encryptionService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        try {
            encryptionService.encryptFields(event.getSource());
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt fields", e);
        }
    }

    @Override
    public void onAfterConvert(AfterConvertEvent<Object> event) {
        try {
            encryptionService.decryptFields(event.getSource());
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt fields", e);
        }
    }
}
