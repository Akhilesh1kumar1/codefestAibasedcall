package com.sr.capital.repository.primary;

import com.sr.capital.entity.mongo.CompanySalesDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanySalesDetailsRepository extends MongoRepository<CompanySalesDetails,String> {
}
