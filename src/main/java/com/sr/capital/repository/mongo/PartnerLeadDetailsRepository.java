package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.PartnerLeadDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartnerLeadDetailsRepository extends MongoRepository<PartnerLeadDetails, String> {

    PartnerLeadDetails findBySrCompanyId(Long srCompanyId);

    PartnerLeadDetails findByPartnerId(Long partnerId);

    PartnerLeadDetails findByLeadId(String leadId);

}
