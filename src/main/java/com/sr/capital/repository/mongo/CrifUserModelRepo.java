package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.crif.CrifUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CrifUserModelRepo extends MongoRepository<CrifUserModel, String> {

    Optional<CrifUserModel> findByMobile(String mobile);
    Optional<CrifUserModel> findByVerificationToken(UUID verificationToken);


    @Query("{ 'created_by': ?0 }")
    List<CrifUserModel> findByCreatedBy(String createdBy);

    Optional<CrifUserModel> findByMobileAndDocumentTypeAndDocumentValue(String encryptedMobileNumber, String docType, String encryptedDocValue);

    List<CrifUserModel> findAllByConsentIdIn(List<String> consentIdList);

    List<CrifUserModel> findByMobileIn(List<String> encryptedMobileNumberList);
}