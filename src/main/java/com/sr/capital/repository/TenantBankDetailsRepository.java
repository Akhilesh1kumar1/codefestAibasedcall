package com.sr.capital.repository;

import com.sr.capital.entity.TenantBankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TenantBankDetailsRepository extends JpaRepository<TenantBankDetails, UUID> {


    List<TenantBankDetails> findBySrCompanyId(Long userId);

    TenantBankDetails findByAccountNumberAndSrCompanyId(String accountNumber , Long srCompanyId);
}
