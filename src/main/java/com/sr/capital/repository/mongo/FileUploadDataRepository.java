package com.sr.capital.repository.mongo;

import com.sr.capital.entity.FileUploadData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface FileUploadDataRepository extends MongoRepository<FileUploadData, String> {

    FileUploadData findByTenantIdAndCorrelationId(String tenantId, String correlationId);

    Page<FileUploadData> findByTenantIdAndCreatedAtBetween(String tenantId, LocalDate fromDate, LocalDate toDate,
                                                           PageRequest pageRequest, Sort sortBy);
}
