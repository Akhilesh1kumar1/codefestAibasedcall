package com.sr.capital.external.truthscreen.repository;

import com.sr.capital.external.truthscreen.entity.PanDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PanRepository extends MongoRepository<PanDetails,String> {


}
