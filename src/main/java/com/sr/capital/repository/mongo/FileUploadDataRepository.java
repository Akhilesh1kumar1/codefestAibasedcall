package com.sr.capital.repository.mongo;

import com.sr.capital.dto.response.FileUploadDataDTO;
import com.sr.capital.entity.primary.FileUploadData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FileUploadDataRepository extends MongoRepository<FileUploadData, String> {

    FileUploadData findByTenantIdAndCorrelationId(String tenantId, String correlationId);

    Page<FileUploadData> findByTenantIdAndCreatedAtBetween(String tenantId, LocalDate fromDate, LocalDate toDate,
                                                           PageRequest pageRequest, Sort sortBy);
    FileUploadData findByTenantIdAndUploadedBy(String tenantId, Long uploadedBy);
    FileUploadData findByTenantIdAndUploadedByAndFileNameAndCorrelationId(String tenantId, Long uploadedBy, String fileName, String correlationId);

    List<FileUploadData> findAllByUploadedByIn(List<Long> uploadedBy);

//    FileUploadData findByTenantIdAndUploadedByAndFileName(String tenantId, Long uploadedBy, String fileName);
}
