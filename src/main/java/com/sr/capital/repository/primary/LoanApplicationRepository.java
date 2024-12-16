package com.sr.capital.repository.primary;

import com.sr.capital.dto.response.LoanApplicationStatusDto;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.helpers.enums.LoanStatus;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, UUID> {

    List<LoanApplication> findBySrCompanyIdAndIsEnabled(Long srCompanyId,Boolean isEnabled);

    List<LoanApplication> findBySrCompanyId(Long srCompanyId);

    @Query("SELECT la from LoanApplication la where la.srCompanyId = :srCompanyId order by la.auditData.createdAt asc")
    List<LoanApplication> findByCompanyIdOrderByCreatedAtAsc(Long srCompanyId);

    List<LoanApplication> findBySrCompanyIdAndLoanStatus(Long srCompanyId, LoanStatus loanStatus);


   /* @Query(value = "SELECT new  com.sr.capital.dto.response.LoanApplicationStatusDTO(la.id, la.srCompanyId, la.loanVendorId,la.loanAmountRequested,la.loanStatus,la.loanOfferId,la.loanDuration, las.id, las.vendorLoanId,las.loanAmountApproved,las.interestRate,las.loanDuration,las.startDate,las.endDate,la.auditData.createdAt,la.auditData.createdBy,las.auditData.createdAt,las.auditData.createdBy,las.auditData.updatedAt) " +
            "FROM LoanApplication la " +
            "LEFT JOIN LoanApplicationStatus las ON la.id = las.loanId " +
            "WHERE la.srCompanyId = :srCompanyId")
    List<LoanApplicationStatusDto> findLoanApplicationWithStatus(@Param("srCompanyId") Long srCompanyId);
*/

    /*@Query( "SELECT new com.sr.capital.dto.response.LoanApplicationStatusDTO(la.id, la.srCompanyId, la.loanVendorId,la.loanAmountRequested) " +
            "FROM LoanApplication la " +
            "WHERE la.srCompanyId = :srCompanyId")
    List<LoanApplicationStatusDto> findLoanApplicationWithStatusNew(@Param("srCompanyId") Long srCompanyId);*/


    @Query(value = "SELECT la.id, la.sr_company_id, la.loan_vendor_id,la.loan_amount_requested ,la.loanAmountRequested,la.loan_status,la.loan_offer_id,la.loan_duration, las.id, las.vendor_loan_id,las.loan_amount_approved,las.interest_rate,las.loan_duration,las.start_date,las.end_date,la.created_at,la.created_by,las.created_at,las.created_by,las.updated_at" +
            "FROM loan_application la " +
            "JOIN loan_application_status las ON la.id = las.loan_id " +
            "WHERE la.sr_company_id = :srCompanyId", nativeQuery = true)
    List<Object[]> findLoanApplicationsWithStatusBySrCompanyId(Long srCompanyId);

    LoanApplication findByVendorLoanId(String vendorLoanId);

   /* @Query(
            value = "SELECT c FROM loan_application c  WHERE c.created_at BETWEEN :startDate AND :endDate",
            countQuery = "SELECT count(*) FROM child_entity_table WHERE audit_data_created_at BETWEEN :startDate AND :endDate",
            nativeQuery = true
    )*/
    Page<LoanApplication> findByLoanStatusAndAuditDataCreatedAtBetween(LoanStatus loanStatus, LocalDateTime startDate,LocalDateTime endDate, Pageable pageable);


    @Query(value = "SELECT la FROM loan_application la WHERE la.created_at BETWEEN :startDate AND :endDate AND la.loan_status IN :loanStatuses ",nativeQuery = true)
    Page<LoanApplication> findAllByDateRangeAndStatusList(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate,@Param("loanStatuses") List<LoanStatus> loanStatuses,
                                             Pageable pageable);

}
