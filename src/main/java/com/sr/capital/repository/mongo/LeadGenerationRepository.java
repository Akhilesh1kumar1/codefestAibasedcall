package com.sr.capital.repository.mongo;

import com.sr.capital.entity.mongo.Lead;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeadGenerationRepository extends MongoRepository<Lead,String> {
    List<Lead> findBySrCompanyIdOrderByCreatedAtDesc(Long srCompanyId);

    @Query("{ 'createdAt': { $gte: ?0, $lte: ?1  } }")
    Page<Lead> findByCreatedAtBetween(LocalDateTime startDate,LocalDateTime endDate, Pageable pageable);

    @Query("{ 'lastModifiedAt': { $gte: ?0, $lte: ?1  } }")
    Page<Lead> findByLastModifiedAtBetween(LocalDateTime startDate,LocalDateTime endDate, Pageable pageable);

}
