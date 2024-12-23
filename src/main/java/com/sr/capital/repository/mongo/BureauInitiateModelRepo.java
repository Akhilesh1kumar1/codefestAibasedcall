package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.crif.BureauInitiateModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BureauInitiateModelRepo extends MongoRepository<BureauInitiateModel, String> {

    Optional<BureauInitiateModel> findByReportIdAndOrderId(String reportId, String orderId);
    List<BureauInitiateModel> findByMobile(String mobile);
}