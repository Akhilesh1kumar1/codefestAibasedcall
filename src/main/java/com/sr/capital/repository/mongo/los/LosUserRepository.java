package com.sr.capital.repository.mongo.los;


import com.sr.capital.entity.mongo.los.LosUserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LosUserRepository extends MongoRepository<LosUserEntity, String> {
    Optional<LosUserEntity> findByMobile(String mobile);
    Optional<LosUserEntity> findByMobileAndSrCompanyId(String mobile, String srCompanyId);

    @Query("{ 'created_by': ?0 }")
    List<LosUserEntity> findByCreatedBy(String createdBy);

    LosUserEntity findBySrCompanyId(String srCompanyId);
}
