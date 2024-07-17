package com.sr.capital.service.impl;

import com.sr.capital.config.db.CommonJdbcUtill;
import com.sr.capital.dto.request.IcrmLeadRequestDto;
import com.sr.capital.dto.response.IcrmLeadCompleteDetails;
import com.sr.capital.dto.response.IcrmLeadRsponseDto;
import com.sr.capital.dto.response.LoanApplicationStatusDto;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.LoanApplicationStatus;
import com.sr.capital.entity.primary.LoanDisbursed;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.kyc.service.DocDetailsService;
import com.sr.capital.service.FileUploadService;
import com.sr.capital.service.IcrmLeadService;
import com.sr.capital.service.LoanApplicationService;
import com.sr.capital.service.entityimpl.LoanApplicationStatusEntityServiceImpl;
import com.sr.capital.service.entityimpl.LoanDistributionEntityServiceImpl;
import com.sr.capital.util.CoreUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.xa.XAException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class IcrmLeadServiceImpl implements IcrmLeadService {

    final LoanApplicationService loanApplicationService;

    final CommonJdbcUtill commonJdbcUtill;

    final LoanDistributionEntityServiceImpl loanDistributionEntityService;

    final LoanApplicationStatusEntityServiceImpl loanApplicationStatusEntityService;

    final DocDetailsService docDetailsService;
    final FileUploadService fileUploadService;

    String FIELDS = "la.id, la.sr_company_id, la.loan_vendor_id,la.loan_amount_requested ,la.loan_amount_requested,la.loan_status,la.loan_type,la.loan_offer_id,la.loan_duration, las.id as loanApplicationStatusId, las.vendor_loan_id,las.loan_amount_approved,las.interest_rate,las.loan_duration,las.start_date,las.end_date,la.created_at as loanCreatedAt,la.created_by as loanCreatedBy,las.created_at as loanApplicationStatusCreatedAt,las.created_by as loanApplicationStatusCreatedBy,las.updated_at as loanApplicationStatusUpdatedAt,las.total_disbursed_amount";

    @Override
    public IcrmLeadRsponseDto getLeadDetails(IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException, ParseException, IOException {
        List<LoanApplicationStatusDto> loanApplicationStatuses =new ArrayList<>();

        IcrmLeadRsponseDto responseDto = IcrmLeadRsponseDto.builder().build();
        List<Map<String, Object>> listRecords = getLoanApplicationByCriteria(icrmLeadRequestDto,responseDto);
        if(CollectionUtils.isNotEmpty(listRecords)){
           return buildLeadResponse(loanApplicationStatuses,listRecords);
        }
       return null;
    }

    @Override
    public IcrmLeadRsponseDto getCompleteLoanDetails(IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException {

        if(icrmLeadRequestDto.getLoanId()==null){
            throw new CustomException("Invalid Request. loan id is required",HttpStatus.BAD_REQUEST);
        }

        if(icrmLeadRequestDto.getLoanApplicationStatusId()==null){
            throw new CustomException("Invalid Request. loan application status id is required",HttpStatus.BAD_REQUEST);
        }
        IcrmLeadRsponseDto icrmLeadRsponseDto = IcrmLeadRsponseDto.builder().completeDetails(new ArrayList<>()).build();
        getLoanDetails(icrmLeadRsponseDto,icrmLeadRequestDto);
        getLoanApplicationStatusDetails(icrmLeadRsponseDto,icrmLeadRequestDto);
        getDisbursedAmount(icrmLeadRsponseDto,icrmLeadRequestDto);
        if(icrmLeadRequestDto.getIsTesting())
            getDocDetails(icrmLeadRsponseDto,icrmLeadRequestDto);

        return icrmLeadRsponseDto;
    }

    private void getDocDetails(IcrmLeadRsponseDto icrmLeadRsponseDto, IcrmLeadRequestDto icrmLeadRequestDto) {

        List<KycDocDetails<?>> kycDocDetails = docDetailsService.fetchDocDetailsByTenantId(String.valueOf(icrmLeadRequestDto.getSrCompanyId()));
        if(CollectionUtils.isNotEmpty(kycDocDetails)){
            icrmLeadRsponseDto.getCompleteDetails().get(0).setZipLink(fileUploadService.downloadAndAddFileToZip(kycDocDetails));
        }
    }

    private void getLoanApplicationStatusDetails(IcrmLeadRsponseDto icrmLeadRsponseDto, IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException {
        LoanApplicationStatus loanApplicationStatus =loanApplicationStatusEntityService.getLoanApplicationStatusById(icrmLeadRequestDto.getLoanApplicationStatusId());
        if(loanApplicationStatus!=null){
            icrmLeadRsponseDto.getCompleteDetails().get(0).setLoanApplicationStatusId(loanApplicationStatus.getId());
            icrmLeadRsponseDto.getCompleteDetails().get(0).setExternalLoanStatus(loanApplicationStatus.getVendorStatus());
            icrmLeadRsponseDto.getCompleteDetails().get(0).setExternalLoanId(loanApplicationStatus.getVendorLoanId());
            icrmLeadRsponseDto.getCompleteDetails().get(0).setLoanDurationAtSanction(loanApplicationStatus.getLoanDuration());
            icrmLeadRsponseDto.getCompleteDetails().get(0).setSanctionAmount(loanApplicationStatus.getLoanAmountApproved());
            icrmLeadRsponseDto.getCompleteDetails().get(0).setInternalLoanId(loanApplicationStatus.getLoanId());
            icrmLeadRsponseDto.getCompleteDetails().get(0).setInterestRate(loanApplicationStatus.getInterestRate());
            icrmLeadRsponseDto.getCompleteDetails().get(0).setInterestAmountAtSanction(loanApplicationStatus.getInterestAmountAtSanction());
            icrmLeadRsponseDto.getCompleteDetails().get(0).setDisbursedAmount(loanApplicationStatus.getTotalDisbursedAmount());
            icrmLeadRsponseDto.getCompleteDetails().get(0).setVendorStatus(loanApplicationStatus.getVendorStatus());
        }else{
            throw new CustomException("Invalid loan application status id",HttpStatus.BAD_REQUEST);
        }
    }

    private void getLoanDetails(IcrmLeadRsponseDto icrmLeadRsponseDto, IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException {

        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(icrmLeadRequestDto.getLoanId());

        if(loanApplication!=null){
            setLoanApplicationDetails(loanApplication,icrmLeadRsponseDto);
        }else{
            throw new CustomException("Invalid Loan Id ",HttpStatus.BAD_REQUEST);
        }

    }

    private void setLoanApplicationDetails(LoanApplication loanApplication, IcrmLeadRsponseDto icrmLeadRsponseDto) {
       IcrmLeadCompleteDetails icrmLeadCompleteDetails =IcrmLeadCompleteDetails.builder().internalLoanId(loanApplication.getId()).loanVendorId(loanApplication.getLoanVendorId())
               .updatedAt(loanApplication.getAuditData().getUpdatedAt()).loanType(loanApplication.getLoanType()).loanStatus(loanApplication.getLoanStatus()).createdAt(loanApplication.getAuditData().getCreatedAt()).dateOfInitiation(loanApplication.getAuditData().getCreatedAt()).build();
       icrmLeadRsponseDto.getCompleteDetails().add(icrmLeadCompleteDetails);
    }

    private void getDisbursedAmount(IcrmLeadRsponseDto icrmLeadRsponseDto, IcrmLeadRequestDto icrmLeadRequestDto) {
        List<LoanDisbursed> loanDisbursedList = loanDistributionEntityService.getLoanDisbursedDetailsByStatusId(icrmLeadRequestDto.getLoanApplicationStatusId());
        icrmLeadRsponseDto.getCompleteDetails().get(0).setDisburseDetails(new ArrayList<>());
        if(CollectionUtils.isNotEmpty(loanDisbursedList)){
            loanDisbursedList.forEach(loanDisbursed -> {
               IcrmLeadCompleteDetails.DisburseDetails disburseDetails = IcrmLeadCompleteDetails.DisburseDetails.builder()
                       .disbursedAmount(loanDisbursed.getLoanAmountDisbursed()).interestAmountAtDisbursal(loanDisbursed.getInterestAmountAtDisbursal())
                       .createdAt(loanDisbursed.getAuditData().getCreatedAt()).interestRate(loanDisbursed.getInterestRateAtDisbursal()).tenure(loanDisbursed.getDurationAtDisbursal()).updatedAt(loanDisbursed.getAuditData().getUpdatedAt()).build();

                icrmLeadRsponseDto.getCompleteDetails().get(0).getDisburseDetails().add(disburseDetails);
            });
        }
    }


    private IcrmLeadRsponseDto buildLeadResponse(List<LoanApplicationStatusDto> loanApplicationStatuses,List<Map<String, Object>> listRecords) {
        IcrmLeadRsponseDto icrmLeadRsponseDto = IcrmLeadRsponseDto.builder().completeDetails(new ArrayList<>()).build();
        for(int i=0;i<listRecords.size();i++) {
            Map<String, Object> orderMap = listRecords.get(i);
            IcrmLeadCompleteDetails icrmLeadCompleteDetails = buildCompleteDetails(orderMap);
            ;/*IcrmLeadCompleteDetails.builder().amountApproved(loanApplicationStatusDto.getLoanAmountApproved()).approvedBy(loanApplicationStatusDto.getLoanApprovedBy()).loanStatus(loanApplicationStatusDto.getLoanStatus()).externalLoanId(loanApplicationStatusDto.getVendorLoanId()).createdAt(loanApplicationStatusDto.getLoanCreatedAt()).internalLoanId(loanApplicationStatusDto.getLoanId()).loanStatus(loanApplicationStatusDto.getLoanStatus()).loanVendorId(loanApplicationStatusDto.getLoanVendorId())
                    .dateOfInitiation(loanApplicationStatusDto.getLoanCreatedAt()).sanctionAmount(loanApplicationStatusDto.getLoanAmountApproved()).creditLineApprovalDate(loanApplicationStatusDto.getLoanApprovedAt()).updatedAt(loanApplicationStatusDto.getLoanStatusUpdatedAt()).build();*/
            icrmLeadRsponseDto.getCompleteDetails().add(icrmLeadCompleteDetails);
        };
        return icrmLeadRsponseDto;
    }


    private  List<Map<String, Object>> getLoanApplicationByCriteria(IcrmLeadRequestDto icrmLeadRequestDto, IcrmLeadRsponseDto responseDto) throws CustomException, ParseException {
        HashMap<String,Object> pageInfo = new HashMap<>();
        Map<String,Integer> mapPagination = new HashMap<>();
        String[] arrSort = new String[1];
        List<Map<String, Object>> listRecords = new ArrayList<>();
        List<String> arrSelectFields = new ArrayList<>( Arrays.asList(FIELDS));
        List<String> arrCountField = new ArrayList<>( Arrays.asList("count(*)"));
        Map<String,Object> mapJoinConditions = new HashMap<>();
        HashMap<String, Object> whereClauseValues = new HashMap<>();
        boolean groupBy=false;
        String []tables = new String[]{"loan_application la ", "loan_application_status las"};

        String loanApplicationPrefix = "la.";
        String loanApplicationStattusPrefix = "las.";
        mapJoinConditions.put("la.id","las.loan_id");



        if(icrmLeadRequestDto.getSrCompanyId()!=null){
            whereClauseValues.put(loanApplicationPrefix+"sr_company_id",icrmLeadRequestDto.getSrCompanyId());
        }

        if(icrmLeadRequestDto.getLoanStatuses()!=null){
            whereClauseValues.put(loanApplicationPrefix+ "loan_status^IN" ,icrmLeadRequestDto.getLoanStatuses());
        }

        if(icrmLeadRequestDto.getExternalLoanIdOfVendor()!=null){
            whereClauseValues.put(loanApplicationStattusPrefix+"vendor_loan_id",icrmLeadRequestDto.getExternalLoanIdOfVendor());
        }

        if(icrmLeadRequestDto.getLoanVendorIds()!=null){
            whereClauseValues.put(loanApplicationPrefix+ "loan_vendor_id^IN" ,icrmLeadRequestDto.getLoanVendorIds());
        }

        if(icrmLeadRequestDto.getCreatedAt()!=null){
            whereClauseValues.put(loanApplicationPrefix+"created_at->=" , icrmLeadRequestDto.getCreatedAt()+" 00:00:00");
        }

        if(icrmLeadRequestDto.getDateOfDisbursal()!=null){
            whereClauseValues.put(loanApplicationStattusPrefix+"created_at->=" , icrmLeadRequestDto.getDateOfDisbursal()+" 00:00:00");
        }

        if(icrmLeadRequestDto.getDateOfSanction()!=null){
            whereClauseValues.put(loanApplicationStattusPrefix+"created_at->=" , icrmLeadRequestDto.getDateOfSanction()+" 00:00:00");
        }

        int offset = (icrmLeadRequestDto.getPageNumber() - 1) * icrmLeadRequestDto.getPageSize();
        int totalNoOfRecords=0;
        int pageSize = icrmLeadRequestDto.getPageSize()==null ? 10: icrmLeadRequestDto.getPageSize();
        if (pageSize < 1) {
            throw new CustomException("Invalid Page Size",HttpStatus.BAD_REQUEST);
        }
        arrSort[0] = loanApplicationPrefix+"created_at desc";

        if(icrmLeadRequestDto.getNoOfRecord()==null){
            pageInfo.put(loanApplicationPrefix+"created_to",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date(System.currentTimeMillis()- TimeUnit.MINUTES.toMillis(5))));
            whereClauseValues.put(loanApplicationPrefix+"created_at-<=", pageInfo.get(loanApplicationPrefix+"created_to"));
            listRecords = commonJdbcUtill.buildAndExecuteQuery(
                    tables
                    , arrCountField
                    , whereClauseValues
                    , mapJoinConditions
                    , null
                    , null
                    , null,groupBy
            );

            if (listRecords == null || listRecords.isEmpty())
                throw new CustomException( "No records found",HttpStatus.NOT_FOUND);

            totalNoOfRecords = Integer.parseInt(listRecords.get(0).get("count(*)").toString());

        }else{
            totalNoOfRecords = icrmLeadRequestDto.getNoOfRecord();
        }

        if(totalNoOfRecords==0){
            throw new CustomException( "No records found",HttpStatus.NOT_FOUND);
        }

        listRecords =  commonJdbcUtill.buildAndExecuteQuery(tables
                ,arrSelectFields
                ,whereClauseValues
                ,mapJoinConditions
                ,arrSort
                ,offset
                ,pageSize,groupBy
        );

        if(listRecords.isEmpty())
            throw new CustomException("No records found",HttpStatus.NOT_FOUND);

        Integer noOfPages=0;
        if(pageSize ==  0 || totalNoOfRecords <  pageSize)
            noOfPages=1;
        else
            noOfPages=(int)Math.ceil( totalNoOfRecords * 1.0 /pageSize);

        IcrmLeadRsponseDto.PaginationInfo paginationInfo = IcrmLeadRsponseDto.PaginationInfo.builder().pageSize(pageSize).noOfRecords(totalNoOfRecords).pageNumber(icrmLeadRequestDto.getPageNumber()).noOfPages(noOfPages).build();
        responseDto.setPaginationInfo(paginationInfo);

        return listRecords;

    }


    private IcrmLeadCompleteDetails buildCompleteDetails(Map<String,Object> map){
        IcrmLeadCompleteDetails dto =IcrmLeadCompleteDetails.builder().build();

        dto.setInternalLoanId(CoreUtil.getUUIDID(map.get("id")));
        dto.setSrCompanyId(Long.valueOf(map.get("sr_company_id").toString()));
        dto.setLoanVendorId(Long.valueOf(map.get("loan_vendor_id").toString()));
        //dto.seta(new BigDecimal(map.get("la.loan_amount_requested").toString()));
        dto.setLoanStatus(LoanStatus.valueOf(map.get("loan_status").toString()));
      //  dto.setLoanOfferId(UUID.fromString(map.get("la.loan_offer_id").toString()));
        dto.setLoanDurationAtSanction(Integer.valueOf(map.get("loan_duration").toString()));
        dto.setExternalLoanId(map.get("vendor_loan_id").toString());
        dto.setSanctionAmount(new BigDecimal(map.get("loan_amount_approved").toString()));
        dto.setLoanType((String) map.get("loan_type"));
        dto.setLoanApplicationStatusId(Long.valueOf(map.get("loanApplicationStatusId").toString()));
       // dto.se(Double.valueOf(map.get("las.interest_rate").toString()));
        /*dto.(LocalDate.parse(map.get("las.start_date").toString()));
        dto.setEndDate(LocalDate.parse(map.get("las.end_date").toString()));*/
        dto.setDateOfInitiation(LocalDateTime.parse(map.get("loanCreatedAt").toString()));
        //dto.setLoanCreatedBy(map.get("loanCreatedBy").toString());
        dto.setCreditLineApprovalDate(LocalDateTime.parse(map.get("loanApplicationStatusCreatedAt").toString()));
        dto.setApprovedBy(map.get("loanApplicationStatusCreatedBy").toString());
        dto.setDisbursedAmount(new BigDecimal(map.get("total_disbursed_amount").toString()));
        //dto.setLoanApplicationStatusUpdatedAt(LocalDateTime.parse(map.get("loanApplicationStatusUpdatedAt").toString()));

        return dto;
    }


}
