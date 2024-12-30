package com.sr.capital.external.crif.util;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.config.MongoFieldEncryptionService;
import com.sr.capital.entity.mongo.crif.CrifUserModel;
import com.sr.capital.repository.mongo.CrifUserModelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CrifUserModelHelper {
    private final CrifUserModelRepo crifUserModelRepo;
    private final AES256 aes256;
    private final MongoFieldEncryptionService mongoFieldEncryptionService;

    public Optional<CrifUserModel> findByMobile(String mobile) {
        String encrypt = aes256.encrypt(mobile);
        return crifUserModelRepo.findByMobile(encrypt);
    }


    public void save(CrifUserModel crifUserModel) {
        crifUserModelRepo.save(crifUserModel);
        try {
            mongoFieldEncryptionService.decryptFields(crifUserModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<CrifUserModel> findByMobileDockTypeAndDocValue(String mobile, String docType, String docValue) {
        String encryptedMobileNumber = aes256.encrypt(mobile);
        String encryptedDocValue = aes256.encrypt(docValue);
        return crifUserModelRepo.findByMobileAndDocumentTypeAndDocumentValue(encryptedMobileNumber, docType, encryptedDocValue);
    }

    public void delete(CrifUserModel crifUserModel) {
        crifUserModelRepo.delete(crifUserModel);
    }
}
