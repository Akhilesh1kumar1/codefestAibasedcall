package com.sr.capital.service.impl;

import com.sr.capital.dto.request.IcrmLoanRequestDto;
import com.sr.capital.dto.response.LoanDetailsDto;
import com.sr.capital.entity.primary.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FetchLoanApplicationCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;




    public long countLoans(IcrmLoanRequestDto icrmLeadRequestDto) {
       /* CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

        *//*Root<LoanApplication> loanApplicationRoot = countQuery.from(LoanApplication.class);
        Root<LoanApplicationStatus> loanApplicationStatus = countQuery.from(LoanApplicationStatus.class);
        Root<LoanDisbursed> loanDisbursed = countQuery.from(LoanDisbursed.class);*//*
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<LoanApplication> loanApplicationRoot = query.from(LoanApplication.class);

        // Joins
        Join<Object, Object> loanStatusJoin = loanApplicationRoot.join("loanApplicationStatus", JoinType.LEFT);
        Join<Object, Object> loanDisbursedJoin = loanApplicationRoot.join("loanDisbursed", JoinType.LEFT);


        List<Predicate> predicates = buildPredicates(icrmLeadRequestDto, cb, loanApplicationRoot, loanStatusJoin,loanDisbursedJoin);


        countQuery.select(cb.count(loanApplicationRoot)).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(countQuery).getSingleResult();*/

        String countSql = "SELECT COUNT(*) FROM loan_application la "
                + "LEFT JOIN loan_application_status las ON la.id = las.loan_id "
                + "LEFT JOIN loan_disbursed ld ON la.id = ld.loan_id ";

        List<String> conditions = buildPredicates(icrmLeadRequestDto);

        if (!conditions.isEmpty()) {
            countSql += " WHERE " + String.join(" AND ", conditions);
        }

        log.info("count query {} ",countSql);
        Query countQuery = entityManager.createNativeQuery(countSql);

        setQueryParameter(countQuery,icrmLeadRequestDto);



        Long totalRecords = ((Number) countQuery.getSingleResult()).longValue();
        return totalRecords;
    }

    public List<LoanDetailsDto> getPaginatedLoanDetails(IcrmLoanRequestDto icrmLeadRequestDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        //CriteriaQuery<LoanDetailsDto> query = cb.createQuery(LoanDetailsDto.class);

        // Root and joins
        /*Root<LoanApplication> loanApplicationRoot = query.from(LoanApplication.class);
        Join<LoanApplication, LoanApplicationStatus> loanStatusJoin = loanApplicationRoot.join("loanApplicationStatus", JoinType.LEFT);
        Join<LoanApplication, LoanDisbursed> loanDisbursedJoin = loanApplicationRoot.join("loanDisbursed", JoinType.LEFT);
*/


        // Create the main query and join tables without mappings
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<LoanApplication> loanApplicationRoot = query.from(LoanApplication.class);

        // Joins
        Join<Object, Object> loanStatusJoin = loanApplicationRoot.join("loanApplicationStatus", JoinType.LEFT);
        Join<Object, Object> loanDisbursedJoin = loanApplicationRoot.join("loanDisbursed", JoinType.LEFT);

        // Select clause - mapping to LoanDetailsDto
        query.multiselect(
                loanApplicationRoot.get(LongBaseEntity.Fields.id).alias(LoanDetailsDto.Fields.loanId) ,
                loanApplicationRoot.get(LoanApplication.Fields.srCompanyId),
                loanApplicationRoot.get(LoanApplication.Fields.loanVendorId),
                loanApplicationRoot.get(LoanApplication.Fields.loanAmountRequested),
                loanApplicationRoot.get(LoanApplication.Fields.loanStatus),
                loanApplicationRoot.get(LoanApplication.Fields.loanType),
                loanApplicationRoot.get(LoanApplication.Fields.loanOfferId),
                loanApplicationRoot.get(LoanApplication.Fields.loanDuration),
                loanApplicationRoot.get(LoanApplication.Fields.vendorLoanId),
                loanApplicationRoot.get(LoanApplication.Fields.vendorStatus),
                loanStatusJoin.get(LongBaseEntity.Fields.id),
                loanApplicationRoot.get(LoanApplication.Fields.vendorLoanId),
                loanStatusJoin.get(LoanApplicationStatus.Fields.loanAmountApproved),
                loanStatusJoin.get(LoanApplicationStatus.Fields.interestRate),
                loanStatusJoin.get(LoanApplicationStatus.Fields.loanDuration),
                loanStatusJoin.get(LoanApplicationStatus.Fields.startDate),
                loanStatusJoin.get(LoanApplicationStatus.Fields.endDate),
                loanApplicationRoot.get(AuditData.Fields.createdAt).alias(LoanDetailsDto.Fields.loanCreatedAt),
                loanApplicationRoot.get(AuditData.Fields.createdBy).alias(LoanDetailsDto.Fields.loanCreatedBy),
                loanStatusJoin.get(AuditData.Fields.createdAt),
                loanStatusJoin.get(AuditData.Fields.createdBy),
                loanStatusJoin.get(AuditData.Fields.updatedAt),
                loanStatusJoin.get(LoanApplicationStatus.Fields.totalDisbursedAmount),
                loanDisbursedJoin.get(LoanDisbursed.Fields.disbursedDate),
                loanDisbursedJoin.get(LoanDisbursed.Fields.emiAmount),
                loanStatusJoin.get(LoanApplicationStatus.Fields.totalRecoverableAmount),
                loanStatusJoin.get(LoanApplicationStatus.Fields.interestAmountAtSanction),
                loanDisbursedJoin.get(LoanDisbursed.Fields.durationAtDisbursal),
                loanDisbursedJoin.get(LoanDisbursed.Fields.interestAmountAtDisbursal).alias(LoanDetailsDto.Fields.interestRateAtDisbursed)
                );

        // Build dynamic predicates
        List<Predicate> predicates = buildPredicates(icrmLeadRequestDto, cb, loanApplicationRoot, loanStatusJoin,loanDisbursedJoin);

        // Apply predicates to the query
        query.where(predicates.toArray(new Predicate[0]));

        // Set ordering if needed
       // query.orderBy(cb.desc(loanApplicationRoot.get("createdAt")));

        // Create TypedQuery for pagination
       /* TypedQuery<LoanDetailsDto> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((icrmLeadRequestDto.getPageNumber()-1) * icrmLeadRequestDto.getPageSize()); // Page index is 0-based
        typedQuery.setMaxResults(icrmLeadRequestDto.getPageSize());*/

        List<Tuple> tuples = entityManager.createQuery(query)
                .setFirstResult((int) icrmLeadRequestDto.getPageNumber())
                .setMaxResults(icrmLeadRequestDto.getPageNumber())
                .getResultList();


        List<LoanDetailsDto> results = new ArrayList<>();
        for (Tuple tuple : tuples) {
            results.add(
                    LoanDetailsDto.builder().loanId(tuple.get("loanId", UUID.class)).build()
            );
/*
new LoanDetailsDto(
                    tuple.get("loanId", UUID.class),
                    tuple.get("srCompanyId", String.class),
                    tuple.get("loanVendorId", String.class),
                    tuple.get("loanAmountRequested", BigDecimal.class),
                    tuple.get("loanStatus", String.class),
                    tuple.get("loanType", String.class),
                    tuple.get("loanOfferId", String.class),
                    tuple.get("loanDuration", Integer.class),
                    tuple.get("loanApplicationStatusId", Long.class),
                    tuple.get("vendorLoanId", String.class),
                    tuple.get("loanAmountApproved", BigDecimal.class),
                    tuple.get("interestRate", BigDecimal.class),
                    tuple.get("startDate", LocalDate.class),
                    tuple.get("endDate", LocalDate.class),
                    tuple.get("disbursedDate", LocalDate.class)
 */
        }

        // Execute and return paginated results
        return results;
    }

    private List<Predicate> buildPredicates(IcrmLoanRequestDto icrmLeadRequestDto,CriteriaBuilder cb, Root<LoanApplication> loanApplicationRoot, Join<Object,Object> loanStatusJoin,Join< Object,Object> loanDisbursedJoind) {
        List<Predicate> predicates = new ArrayList<>();

        if (icrmLeadRequestDto.getSrCompanyId() != null) {
            predicates.add(cb.equal(loanApplicationRoot.get(LoanApplication.Fields.srCompanyId), icrmLeadRequestDto.getSrCompanyId()));
        }

        if (icrmLeadRequestDto.getExternalLoanIdOfVendor() != null) {
            predicates.add(cb.equal(loanApplicationRoot.get(LoanApplication.Fields.vendorLoanId), icrmLeadRequestDto.getExternalLoanIdOfVendor()));
        }
        if (icrmLeadRequestDto.getCreatedAt()!=null &&  icrmLeadRequestDto.getCreatedAtEnd()!=null) {
            predicates.add(cb.between(loanApplicationRoot.get(AuditData.Fields.createdAt), icrmLeadRequestDto.getCreatedAt(), icrmLeadRequestDto.getCreatedAtEnd()));
        }
        if (CollectionUtils.isNotEmpty(icrmLeadRequestDto.getLoanStatuses())) {
            predicates.add(loanApplicationRoot.get(LoanApplication.Fields.loanStatus).in( icrmLeadRequestDto.getLoanStatuses()));
        }

        if(icrmLeadRequestDto.getDateOfDisbursal()!=null && icrmLeadRequestDto.getDateOfDisbursalEnd()!=null){
            predicates.add(cb.between(loanDisbursedJoind.get(LoanDisbursed.Fields.disbursedDate), icrmLeadRequestDto.getCreatedAt(), icrmLeadRequestDto.getCreatedAtEnd()));

        }


        return predicates;
    }


    public List<Object[]> findLoanDetails(IcrmLoanRequestDto icrmLoanRequestDto) {
        // Base native query
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT la.id AS loanId, la.sr_company_id AS srCompanyId, la.loan_vendor_id AS loanVendorId, ");
        sql.append(" la.loan_status AS loanStatus,la.loan_amount_requested AS loanAmountRequested, ");
        sql.append("la.loan_type AS loanType,DATE_FORMAT(la.created_at,'%Y-%m-%d %H:%i:%s') as leadCreatedAt, las.loan_duration AS loanDuration,la.external_loan_id ,");
        sql.append("las.id AS loanApplicationStatusId, ");
        sql.append("las.loan_amount_approved AS loanAmountApproved,las.interest_amount_at_sanction, las.interest_rate AS interestRate, ");
        sql.append("DATE_FORMAT(las.created_at,'%Y-%m-%d %H:%i:%s') AS creditLineApprovalDate, la.updated_by AS approvedBy, ");
        sql.append("las.total_disbursed_amount AS disbursedAmount, ld.duration_at_disbursal AS durationAtDisbursal,ld.interest_rate_at_disbursal AS interestRateAtDisbursal ,");
        sql.append("DATE_FORMAT(ld.disbursed_date,'%Y-%m-%d') AS disbursedDate, ld.emi_amount AS emiAmount ,");
        sql.append("las.total_recoverable_amount AS recoverableAmount, la.vendor_status AS vendorStatus ,ld.interest_amount_at_disbursal ");
        sql.append("FROM loan_application la ");
        sql.append("LEFT JOIN loan_application_status las ON la.id = las.loan_id ");
        sql.append("LEFT JOIN loan_disbursed ld ON la.id = ld.loan_id ");

        // Where conditions based on criteria
        List<String> conditions = buildPredicates(icrmLoanRequestDto);
        // Add additional conditions as needed

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        sql.append(" order by la.created_at desc");

        log.info("select  query {} ",sql);


        // Apply pagination
        Query query = entityManager.createNativeQuery(sql.toString());

        setQueryParameter(query,icrmLoanRequestDto);

        query.setFirstResult((int) icrmLoanRequestDto.getPageNumber()*icrmLoanRequestDto.getPageSize());
        query.setMaxResults(icrmLoanRequestDto.getPageSize());

        // Execute query and map results to DTO
        List<Object[]> loanDetailsList = query.getResultList();



        return loanDetailsList;
    }

    private List<String> buildPredicates(IcrmLoanRequestDto icrmLeadRequestDto) {
        List<String> conditions = new ArrayList<>();

        // Equals condition for srCompanyId
        if (icrmLeadRequestDto.getSrCompanyId() != null) {
            conditions.add("la.sr_company_id = :srCompanyId");
        }

        if(icrmLeadRequestDto.getLoanId()!=null){
            conditions.add("la.id = :loanId");

        }

        // Equals condition for externalLoanIdOfVendor
        if (icrmLeadRequestDto.getExternalLoanIdOfVendor() != null) {
            conditions.add("la.external_loan_id = :externalLoanIdOfVendor");
        }

        // Between condition for createdAt range
        if (icrmLeadRequestDto.getCreatedAt() != null && icrmLeadRequestDto.getCreatedAtEnd() != null) {
            conditions.add("la.created_at BETWEEN :createdAt AND :createdAtEnd");
        }

        // IN condition for loanStatuses
        if (icrmLeadRequestDto.getLoanStatuses() != null && !icrmLeadRequestDto.getLoanStatuses().isEmpty()) {
            conditions.add("la.loan_status IN :loanStatuses");
        }

        // Between condition for dateOfDisbursal range
        if (icrmLeadRequestDto.getDateOfDisbursal() != null && icrmLeadRequestDto.getDateOfDisbursalEnd() != null) {
            conditions.add("ld.disbursed_date BETWEEN :dateOfDisbursal AND :dateOfDisbursalEnd");
        }

        if(CollectionUtils.isNotEmpty(icrmLeadRequestDto.getLoanVendorIds())){
            conditions.add("la.loan_vendor_id IN :loanVendorIds");
        }

        // Additional conditions can be added similarly...

        return conditions;

    }

    private void setQueryParameter(Query query,IcrmLoanRequestDto icrmLeadRequestDto){
        // Setting parameters for each condition if they are not null
        if (icrmLeadRequestDto.getSrCompanyId() != null) {
            query.setParameter("srCompanyId", icrmLeadRequestDto.getSrCompanyId());
        }

        if(icrmLeadRequestDto.getLoanId()!=null){
            query.setParameter("loanId", icrmLeadRequestDto.getLoanId());

        }
        if (icrmLeadRequestDto.getExternalLoanIdOfVendor() != null) {
            query.setParameter("externalLoanIdOfVendor", icrmLeadRequestDto.getExternalLoanIdOfVendor());
        }

        if (icrmLeadRequestDto.getCreatedAt() != null && icrmLeadRequestDto.getCreatedAtEnd() != null) {
            query.setParameter("createdAt", icrmLeadRequestDto.getCreatedAt());
            query.setParameter("createdAtEnd", icrmLeadRequestDto.getCreatedAtEnd());
        }

        if (icrmLeadRequestDto.getLoanStatuses() != null && !icrmLeadRequestDto.getLoanStatuses().isEmpty()) {
            query.setParameter("loanStatuses", icrmLeadRequestDto.getLoanStatuses());
        }

        if (icrmLeadRequestDto.getDateOfDisbursal() != null && icrmLeadRequestDto.getDateOfDisbursalEnd() != null) {
            query.setParameter("dateOfDisbursal", icrmLeadRequestDto.getDateOfDisbursal());
            query.setParameter("dateOfDisbursalEnd", icrmLeadRequestDto.getDateOfDisbursalEnd());
        }

        if(CollectionUtils.isNotEmpty(icrmLeadRequestDto.getLoanVendorIds())){
            query.setParameter("loanVendorIds", icrmLeadRequestDto.getLoanVendorIds());

        }
    }

}
