package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.kyc.KycVerifiedDetails;
import com.sr.capital.helpers.enums.TaskType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KycVerifiedDetailsRepo extends MongoRepository<KycVerifiedDetails<?>, String> {

    @Query("{ 'sr_company_id': ?0, 'task_type': ?1}")
    KycVerifiedDetails<?> findBySrCompanyIdAndTaskType(String tenantId, TaskType taskType);

    List<KycVerifiedDetails<?>> findBySrCompanyId(String tenantId);

}
