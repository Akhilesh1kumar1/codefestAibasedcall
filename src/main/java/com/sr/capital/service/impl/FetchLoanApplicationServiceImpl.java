package com.sr.capital.service.impl;

import com.sr.capital.dto.request.IcrmLeadRequestDto;
import com.sr.capital.dto.request.IcrmLoanRequestDto;
import com.sr.capital.dto.response.LoanDetailsDto;
import com.sr.capital.entity.primary.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FetchLoanApplicationServiceImpl {

    @PersistenceContext
    private EntityManager entityManager;




    public long countLoans(IcrmLoanRequestDto icrmLeadRequestDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

        Root<LoanApplication> loanApplicationRoot = countQuery.from(LoanApplication.class);
        Join<LoanApplication, LoanApplicationStatus> loanStatusJoin = loanApplicationRoot.join("loanApplicationStatus", JoinType.LEFT);
        Join<LoanApplication, LoanDisbursed> loanDisbursedJoin = loanApplicationRoot.join("loanDisbursed", JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(icrmLeadRequestDto, cb, loanApplicationRoot, loanStatusJoin,loanDisbursedJoin);


        countQuery.select(cb.count(loanApplicationRoot)).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    public List<LoanDetailsDto> getPaginatedLoanDetails(IcrmLoanRequestDto icrmLeadRequestDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LoanDetailsDto> query = cb.createQuery(LoanDetailsDto.class);

        // Root and joins
        Root<LoanApplication> loanApplicationRoot = query.from(LoanApplication.class);
        Join<LoanApplication, LoanApplicationStatus> loanStatusJoin = loanApplicationRoot.join("loanApplicationStatus", JoinType.LEFT);
        Join<LoanApplication, LoanDisbursed> loanDisbursedJoin = loanApplicationRoot.join("loanDisbursed", JoinType.LEFT);

        // Select clause - mapping to LoanDetailsDto
        query.select(cb.construct(
                LoanDetailsDto.class,
                loanApplicationRoot.get(LongBaseEntity.Fields.id),
                loanApplicationRoot.get(LoanApplication.Fields.srCompanyId),
                loanApplicationRoot.get(LoanApplication.Fields.loanVendorId),
                loanApplicationRoot.get(LoanApplication.Fields.loanAmountRequested),
                loanApplicationRoot.get(LoanApplication.Fields.loanStatus),
                loanApplicationRoot.get(LoanApplication.Fields.loanType),
                loanApplicationRoot.get(LoanApplication.Fields.loanOfferId),
                loanApplicationRoot.get(LoanApplication.Fields.loanDuration),
                loanStatusJoin.get(LongBaseEntity.Fields.id),
                loanStatusJoin.get(LoanApplication.Fields.vendorLoanId),
                loanStatusJoin.get(LoanApplicationStatus.Fields.loanAmountApproved),
                loanStatusJoin.get(LoanApplicationStatus.Fields.interestRate),
                loanStatusJoin.get(LoanApplicationStatus.Fields.loanDuration),
                loanStatusJoin.get(LoanApplicationStatus.Fields.startDate),
                loanStatusJoin.get(LoanApplicationStatus.Fields.endDate),
                loanApplicationRoot.get(AuditData.Fields.createdAt),
                loanApplicationRoot.get(AuditData.Fields.createdBy),
                loanStatusJoin.get(AuditData.Fields.createdAt),
                loanStatusJoin.get(AuditData.Fields.createdBy),
                loanStatusJoin.get(AuditData.Fields.updatedAt),
                loanStatusJoin.get(LoanApplicationStatus.Fields.totalDisbursedAmount),
                loanDisbursedJoin.get(LoanDisbursed.Fields.disbursedDate)
        ));

        // Build dynamic predicates
        List<Predicate> predicates = buildPredicates(icrmLeadRequestDto, cb, loanApplicationRoot, loanStatusJoin,loanDisbursedJoin);

        // Apply predicates to the query
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // Set ordering if needed
        query.orderBy(cb.desc(loanApplicationRoot.get("createdAt")));

        // Create TypedQuery for pagination
        TypedQuery<LoanDetailsDto> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((icrmLeadRequestDto.getPageNumber()-1) * icrmLeadRequestDto.getPageSize()); // Page index is 0-based
        typedQuery.setMaxResults(icrmLeadRequestDto.getPageSize());

        // Execute and return paginated results
        return typedQuery.getResultList();
    }

    private List<Predicate> buildPredicates(IcrmLoanRequestDto icrmLeadRequestDto,CriteriaBuilder cb, Root<LoanApplication> loanApplicationRoot, Join<LoanApplication, LoanApplicationStatus> loanStatusJoin,Join<LoanApplication, LoanDisbursed> loanDisbursedJoind) {
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
            predicates.add((loanApplicationRoot.get(LoanApplication.Fields.loanStatus).in( icrmLeadRequestDto.getLoanStatuses()));
        }

        if(icrmLeadRequestDto.getDateOfDisbursal()!=null && icrmLeadRequestDto.getDateOfDisbursalEnd()!=null){
            predicates.add(cb.between(loanDisbursedJoind.get(LoanDisbursed.Fields.disbursedDate), icrmLeadRequestDto.getCreatedAt(), icrmLeadRequestDto.getCreatedAtEnd()));

        }


        return predicates;
    }


}
