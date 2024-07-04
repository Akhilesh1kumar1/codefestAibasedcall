package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.kyc.GstCompleteDocDetails;
import com.sr.capital.entity.mongo.kyc.KycDocDetailsHistory;
import com.sr.capital.helpers.enums.DocType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GstCompleteDocDetailsRepository extends MongoRepository<GstCompleteDocDetails, String> {

    GstCompleteDocDetails findBySrCompanyIdAndGstInNumber(Long tenantId, String gstInId);

    List<GstCompleteDocDetails> findBySrCompanyId(Long tenantId);
}
