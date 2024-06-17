package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.helpers.enums.VerificationStatus;
import com.sr.capital.helpers.enums.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationEntity, Long> {



    @Query("SELECT ver from VerificationEntity ver where ver.srCompanyId = :tenantId and ver.type = :verificationType and status IN :statusList order by ver.auditData.createdAt desc")
    List<VerificationEntity> findByTenantIdVerificationTypeAndStatusList(String tenantId, VerificationType verificationType, List<VerificationStatus> statusList);
}
