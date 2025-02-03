package com.sr.capital.external.corpveda.repository;

import com.sr.capital.external.corpveda.docs.CorpVedaDocDetails;
import com.sr.capital.external.corpveda.enums.CorpVedaDocType;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorpVedaDocDetailsRepository extends MongoRepository<CorpVedaDocDetails<?>,String> {

    CorpVedaDocDetails<?> findBySrCompanyIdAndCorpVedaDocType(String srCompanyId, CorpVedaDocType corpVedaDocType);
}
