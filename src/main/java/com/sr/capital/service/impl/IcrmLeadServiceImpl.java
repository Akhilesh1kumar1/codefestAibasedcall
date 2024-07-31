package com.sr.capital.service.impl;

import com.amazonaws.HttpMethod;
import com.opencsv.CSVWriter;
import com.sr.capital.config.AppProperties;
import com.sr.capital.config.db.CommonJdbcUtill;
import com.sr.capital.dto.request.GenerateLeadRequestDto;
import com.sr.capital.dto.request.IcrmLeadDetailsRequestDto;
import com.sr.capital.dto.request.IcrmLeadRequestDto;
import com.sr.capital.dto.response.*;
import com.sr.capital.dto.response.event.Action;
import com.sr.capital.dto.response.event.Event;
import com.sr.capital.dto.response.event.Events;
import com.sr.capital.dto.response.event.Transitions;
import com.sr.capital.entity.mongo.Lead;
import com.sr.capital.entity.mongo.LeadHistory;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.primary.*;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.dto.response.KaleyraResponse;
import com.sr.capital.external.service.CommunicationService;
import com.sr.capital.helpers.enums.LeadStatus;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.kyc.dto.request.GeneratePreSignedUrlRequest;
import com.sr.capital.kyc.service.DocDetailsService;
import com.sr.capital.service.*;
import com.sr.capital.service.entityimpl.*;
import com.sr.capital.util.CoreUtil;
import com.sr.capital.util.S3Util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sr.capital.helpers.constants.Constants.REQUIRED_DOCUMENTS;

@Service
@RequiredArgsConstructor
public class IcrmLeadServiceImpl implements IcrmLeadService {

    final LoanApplicationService loanApplicationService;

    final CommonJdbcUtill commonJdbcUtill;

    final LoanDistributionEntityServiceImpl loanDistributionEntityService;

    final LoanApplicationStatusEntityServiceImpl loanApplicationStatusEntityService;

    final DocDetailsService docDetailsService;

    final FileUploadService fileUploadService;

    final LeadGenerationService leadGenerationService;

    final CompanyWiseReportEntityServiceImpl companyWiseReportEntityService;

    final UserService userService;

    final AppProperties appProperties;

    final CommunicationService communicationService;

    final LeadHistoryServiceImpl leadHistoryService;

    final BaseCreditPartnerEntityServiceImpl baseCreditPartnerEntityService;

    final WhatsAppEntityServiceImpl whatsAppEntityService;

    @Autowired
    @Qualifier("leadEvents")
    Events events;

    Map<String, List<Map<String, String>>> eventsMap = new HashMap<String, List<Map<String, String>>>();

    @PostConstruct
    public void init() {
        for (Entry<String, Event> entry : events.getEvents().entrySet()) {
            for (Map.Entry<Transitions, Action> transition : entry.getValue().getTransition().entrySet()) {
                String currentStatus = transition.getKey().getStatus().name();
                if (eventsMap.get(currentStatus) == null) {
                    eventsMap.put(currentStatus, new ArrayList<>());
                }
                Map<String, String> statusMap = new HashMap<>();
                statusMap.put("value", transition.getValue().getState().getStatus().name());
                statusMap.put("display_value", transition.getValue().getState().getStatus().getDisplayName());
                eventsMap
                        .get(currentStatus)
                        .add(statusMap);
            }
        }
    }

    String FIELDS = "la.id, la.sr_company_id, la.loan_vendor_id,la.loan_amount_requested ,la.loan_amount_requested,la.loan_status,la.loan_type,la.loan_offer_id,la.loan_duration, las.id as loanApplicationStatusId, las.vendor_loan_id,las.loan_amount_approved,las.interest_rate,las.loan_duration,las.start_date,las.end_date,la.created_at as loanCreatedAt,la.created_by as loanCreatedBy,las.created_at as loanApplicationStatusCreatedAt,las.created_by as loanApplicationStatusCreatedBy,las.updated_at as loanApplicationStatusUpdatedAt,las.total_disbursed_amount";

    @Override
    public IcrmLoanResponseDto getLoanDetails(IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException, ParseException, IOException {
        List<LoanApplicationStatusDto> loanApplicationStatuses =new ArrayList<>();

        IcrmLoanResponseDto responseDto = IcrmLoanResponseDto.builder().build();
        List<Map<String, Object>> listRecords = getLoanApplicationByCriteria(icrmLeadRequestDto,responseDto);
        if(CollectionUtils.isNotEmpty(listRecords)){
           return buildLeadResponse(responseDto,listRecords);
        }
       return null;
    }



    @Override
    public IcrmLoanResponseDto getCompleteLoanDetails(IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException {

        if(icrmLeadRequestDto.getLoanId()==null){
            throw new CustomException("Invalid Request. loan id is required",HttpStatus.BAD_REQUEST);
        }

        if(icrmLeadRequestDto.getLoanApplicationStatusId()==null){
            throw new CustomException("Invalid Request. loan application status id is required",HttpStatus.BAD_REQUEST);
        }
        IcrmLoanResponseDto icrmLoanResponseDto = IcrmLoanResponseDto.builder().completeDetails(new ArrayList<>()).build();
        getLoanDetails(icrmLoanResponseDto,icrmLeadRequestDto);
        getLoanApplicationStatusDetails(icrmLoanResponseDto,icrmLeadRequestDto);
        getDisbursedAmount(icrmLoanResponseDto,icrmLeadRequestDto);
        getDocDetails(icrmLoanResponseDto,icrmLeadRequestDto);

        return icrmLoanResponseDto;
    }

    @Override
    public GenerateLeadResponseDto updateLead(GenerateLeadRequestDto generateLeadRequestDto) throws CustomException {
        if(generateLeadRequestDto.getStatus().equals(LeadStatus.NOT_CONNECTED)) {
           User user = userService.getCompanyDetails(generateLeadRequestDto.getSrCompanyId());
           KaleyraResponse response = communicationService.sendCommunication(communicationService.getCommunicationRequestForSellerNotConnectedViadWhatsApp(user.getMobile(), List.of(user.getFirstName()), appProperties.getKaleyraWhatsappSellerNotConnectedTemplateName()));
           if(response!=null && CollectionUtils.isNotEmpty(response.getData())){
               response.getData().forEach(data->{
                   WhatsappApiLog whatsappApiLog= WhatsappApiLog.builder().messageId(data.getId()).remarks(data.getStatus()).internalId(generateLeadRequestDto.getLeadId()).eventType("lead").srCompanyId(generateLeadRequestDto.getSrCompanyId()).build();
                   whatsAppEntityService.saveWhatsAppApiLog(whatsappApiLog);
               });
           }

            communicationService.sendEmail(communicationService.getCommunicationRequestForSellerNotConnect(user.getEmail(),user.getFirstName()));

        }
        return leadGenerationService.updateLead(generateLeadRequestDto);
    }

    @Override
    public LeadDetailsResponseDto getAllLeads(IcrmLeadDetailsRequestDto icrmLeadRequestDto, Pageable pageable) {
        Page<Lead> leads =leadGenerationService.getAllLeads(icrmLeadRequestDto,pageable);
        List<LeadDetailsResponseDto.LeadDetails> leadDetails =new ArrayList<>();
        leads.forEach(lead -> {
            LeadDetailsResponseDto.LeadDetails leadDto =   LeadDetailsResponseDto.LeadDetails.builder()
                    .srCompanyId(lead.getSrCompanyId())
                    .amount(lead.getAmount())
                    .duration(lead.getDuration())
                    .status(lead.getStatus())
                    .loanApplicationId(lead.getLoanApplicationId())
                    .tier(lead.getTier())
                    .leadSource(lead.getLeadSource())
                    .remarks(lead.getRemarks())
                    .loanVendorPartnerId(lead.getLoanVendorPartnerId())
                    .leadId(lead.getId())
                    .createdAt(lead.getCreatedAt())
                    .updatedAt(lead.getLastModifiedAt()).userName(lead.getUserName()).leadSource(lead.getLeadSource())
                    .build();

            User user = userService.getCompanyDetails(lead.getSrCompanyId());
            if(user!=null){
                leadDto.setCompanyName(user.getCompanyName());
            }
            //TODO
            /*CompanyWiseReport companyWiseReport = companyWiseReportEntityService.getReportData(lead.getSrCompanyId());
            if(companyWiseReport!=null){
                leadDto.setTier(companyWiseReport.getTier());
                leadDto.setCompanyName(companyWiseReport.getCompanyTag());
                //leadDto.setBrandName(companyWiseReport.getBrandNameAmazon()); TODO
            }*/
            leadDetails.add(leadDto);

        });
        return LeadDetailsResponseDto.builder().leadList(leadDetails).pageNumber(leads.getNumber()).totalPages(leads.getTotalPages()).pageSize(leads.getSize()).totalElements(leads.getTotalElements()).build();
    }

    @Override
    @Async
    public void downloadLoanReport(IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException, ParseException, IOException {

        List<IcrmLoanCompleteDetails> icrmLoanCompleteDetails =new ArrayList<>();
        int page =1;
        icrmLeadRequestDto.setPageNumber(1);
        icrmLeadRequestDto.setPageSize(50);
        IcrmLoanResponseDto responseDto = getLoanDetails(icrmLeadRequestDto);
         List<BaseCreditPartnerResponseDto> baseCreditPartnerResponseDtos = baseCreditPartnerEntityService.getAllCreditPartner();
        Map<Long, String> idNameMap = baseCreditPartnerResponseDtos.stream()
                .collect(Collectors.toMap(BaseCreditPartnerResponseDto::getId, BaseCreditPartnerResponseDto::getCreditPartnerName));

        if(responseDto.getPaginationInfo()!=null ){
            icrmLeadRequestDto.setNoOfRecord(responseDto.getPaginationInfo().getNoOfRecords());
            icrmLoanCompleteDetails.addAll(responseDto.getCompleteDetails());

            while (page<responseDto.getPaginationInfo().getNoOfPages()){
                icrmLeadRequestDto.setPageNumber(icrmLeadRequestDto.getPageNumber()+1);
                responseDto  =getLoanDetails(icrmLeadRequestDto);
                try {
                    if (CollectionUtils.isNotEmpty(responseDto.getCompleteDetails())) {
                    icrmLoanCompleteDetails.addAll(responseDto.getCompleteDetails());
                    }
                }catch (Exception ex){
                    break;
                }
                page++;
            }
        }

        String content = convertLoanToCSVAndEncodeBase64(icrmLoanCompleteDetails,idNameMap);
        communicationService.sendEmail(communicationService.getCommunicationRequestForReport(icrmLeadRequestDto.getUserEmail(), "User", content,"Capital Loan Report "+new Date(),"loan_report.csv",true));


    }

    @Override
    @Async
    public void downloadLeadDetails(IcrmLeadDetailsRequestDto icrmLeadDetailsRequestDto) {

        List<LeadDetailsResponseDto.LeadDetails> leadDetails =new ArrayList<>();

        int page = 0;
        int size = 50; // Adjust the page size as needed
        Page<Lead> leadPage;
        LeadDetailsResponseDto responseDto = getAllLeads(icrmLeadDetailsRequestDto, PageRequest.of(page, size));
        Integer totalPages = responseDto.getTotalPages();
        leadDetails.addAll(responseDto.getLeadList());
        while (page<totalPages){
            page++;
            icrmLeadDetailsRequestDto.setPage(page);
            responseDto = getAllLeads(icrmLeadDetailsRequestDto, PageRequest.of(page, size));
            leadDetails.addAll(responseDto.getLeadList());
        }
        String content = convertLeadToCSVAndEncodeBase64(leadDetails);
        communicationService.sendEmail(communicationService.getCommunicationRequestForReport(icrmLeadDetailsRequestDto.getEmailId(), "User", content,"Capital Lead Report "+new Date(),"lead_report.csv",false));


    }

    @Override
    public List<LeadHistoryResponseDto> getLeadHistory(String leadId) {
        List<LeadHistory> leadHistories = leadHistoryService.getLeadHistory(leadId);
        List<LeadHistoryResponseDto> leadHistoryResponseDtos =new ArrayList<>();
        if(CollectionUtils.isNotEmpty(leadHistories)){
            leadHistories.stream().forEach(lead -> {
                LeadHistoryResponseDto responseDto =LeadHistoryResponseDto.builder().srCompanyId(lead.getSrCompanyId()).amount(lead.getAmount()).duration(lead.getDuration()).status(lead.getStatus()).leadSource(lead.getLeadSource()).remarks(lead.getRemarks()).loanApplicationId(lead.getLoanApplicationId())
                        .loanVendorPartnerId(lead.getLoanVendorPartnerId()).tier(lead.getTier()).remarks(lead.getRemarks()).leadId(lead.getId()).userName(lead.getUserName()).build();
                responseDto.setCreatedBy(lead.getCreatedBy());
                responseDto.setLastModifiedBy(lead.getLastModifiedBy());
                responseDto.setCreatedAt(lead.getCreatedAt());
                responseDto.setLastModifiedAt(lead.getLastModifiedAt());
                leadHistoryResponseDtos.add(responseDto);
            });
        }

        return leadHistoryResponseDtos;
    }

    @Override
    public Map<String, List<Map<String, String>>> getEvent() {
        return eventsMap;
    }




    private void getDocDetails(IcrmLoanResponseDto icrmLoanResponseDto, IcrmLeadRequestDto icrmLeadRequestDto) {

        List<KycDocDetails<?>> kycDocDetails = docDetailsService.fetchDocDetailsByTenantId(String.valueOf(icrmLeadRequestDto.getSrCompanyId()));
        if(CollectionUtils.isNotEmpty(kycDocDetails)){
            String zipFilePath =fileUploadService.downloadAndAddFileToZip(kycDocDetails);
            GeneratePreSignedUrlRequest preSignedUrlRequest = GeneratePreSignedUrlRequest.builder()
                    .filePath(zipFilePath)
                    .bucketName(appProperties.getBucketName()).expiry(30)
                    .httpMethod(HttpMethod.GET)
                    .build();
            icrmLoanResponseDto.getCompleteDetails().get(0).setZipLink(S3Util.generatePreSignedUrl(preSignedUrlRequest));

            Set<String> setOfDocs =new HashSet<>();
            kycDocDetails.forEach(kycDocDetails1 -> {
                setOfDocs.add(kycDocDetails1.getDocType().name());
            });
            if(REQUIRED_DOCUMENTS.stream().allMatch(setOfDocs::contains)){
                icrmLoanResponseDto.getCompleteDetails().get(0).setDocumentCompletedAt(kycDocDetails.get(kycDocDetails.size()-1).getCreatedAt());
            }
        }

    }

    private void getLoanApplicationStatusDetails(IcrmLoanResponseDto icrmLoanResponseDto, IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException {
        LoanApplicationStatus loanApplicationStatus =loanApplicationStatusEntityService.getLoanApplicationStatusById(icrmLeadRequestDto.getLoanApplicationStatusId());
        if(loanApplicationStatus!=null){
            icrmLoanResponseDto.getCompleteDetails().get(0).setLoanApplicationStatusId(loanApplicationStatus.getId());
            icrmLoanResponseDto.getCompleteDetails().get(0).setExternalLoanStatus(loanApplicationStatus.getVendorStatus());
            icrmLoanResponseDto.getCompleteDetails().get(0).setExternalLoanId(loanApplicationStatus.getVendorLoanId());
            icrmLoanResponseDto.getCompleteDetails().get(0).setLoanDurationAtSanction(loanApplicationStatus.getLoanDuration());
            icrmLoanResponseDto.getCompleteDetails().get(0).setSanctionAmount(loanApplicationStatus.getLoanAmountApproved());
            icrmLoanResponseDto.getCompleteDetails().get(0).setInternalLoanId(loanApplicationStatus.getLoanId());
            icrmLoanResponseDto.getCompleteDetails().get(0).setInterestRate(loanApplicationStatus.getInterestRate());
            icrmLoanResponseDto.getCompleteDetails().get(0).setInterestAmountAtSanction(loanApplicationStatus.getInterestAmountAtSanction());
            icrmLoanResponseDto.getCompleteDetails().get(0).setDisbursedAmount(loanApplicationStatus.getTotalDisbursedAmount());
            icrmLoanResponseDto.getCompleteDetails().get(0).setVendorStatus(loanApplicationStatus.getVendorStatus());
        }else{
            throw new CustomException("Invalid loan application status id",HttpStatus.BAD_REQUEST);
        }
    }

    private void getLoanDetails(IcrmLoanResponseDto icrmLoanResponseDto, IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException {

        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(icrmLeadRequestDto.getLoanId());

        if(loanApplication!=null){
            setLoanApplicationDetails(loanApplication, icrmLoanResponseDto);
        }else{
            throw new CustomException("Invalid Loan Id ",HttpStatus.BAD_REQUEST);
        }

    }

    private void setLoanApplicationDetails(LoanApplication loanApplication, IcrmLoanResponseDto icrmLoanResponseDto) {
       IcrmLoanCompleteDetails icrmLoanCompleteDetails = IcrmLoanCompleteDetails.builder().internalLoanId(loanApplication.getId()).loanVendorId(loanApplication.getLoanVendorId())
               .updatedAt(loanApplication.getAuditData().getUpdatedAt()).loanType(loanApplication.getLoanType()).loanStatus(loanApplication.getLoanStatus()).createdAt(loanApplication.getAuditData().getCreatedAt()).dateOfInitiation(loanApplication.getAuditData().getCreatedAt()).build();
       icrmLoanResponseDto.getCompleteDetails().add(icrmLoanCompleteDetails);
    }

    private void getDisbursedAmount(IcrmLoanResponseDto icrmLoanResponseDto, IcrmLeadRequestDto icrmLeadRequestDto) {
        List<LoanDisbursed> loanDisbursedList = loanDistributionEntityService.getLoanDisbursedDetailsByStatusId(icrmLeadRequestDto.getLoanApplicationStatusId());
        icrmLoanResponseDto.getCompleteDetails().get(0).setDisburseDetails(new ArrayList<>());
        if(CollectionUtils.isNotEmpty(loanDisbursedList)){
            loanDisbursedList.forEach(loanDisbursed -> {
               IcrmLoanCompleteDetails.DisburseDetails disburseDetails = IcrmLoanCompleteDetails.DisburseDetails.builder()
                       .disbursedAmount(loanDisbursed.getLoanAmountDisbursed()).interestAmountAtDisbursal(loanDisbursed.getInterestAmountAtDisbursal())
                       .createdAt(loanDisbursed.getAuditData().getCreatedAt()).interestRate(loanDisbursed.getInterestRateAtDisbursal()).tenure(loanDisbursed.getDurationAtDisbursal()).updatedAt(loanDisbursed.getAuditData().getUpdatedAt()).build();

                icrmLoanResponseDto.getCompleteDetails().get(0).getDisburseDetails().add(disburseDetails);
            });
        }
    }


    private IcrmLoanResponseDto buildLeadResponse(IcrmLoanResponseDto icrmLoanResponseDto, List<Map<String, Object>> listRecords) {
       // IcrmLoanResponseDto icrmLoanResponseDto = IcrmLoanResponseDto.builder().completeDetails(new ArrayList<>()).build();
        icrmLoanResponseDto.setCompleteDetails(new ArrayList<>());
        Map<Long,User> userMap =new HashMap<>();
        for(int i=0;i<listRecords.size();i++) {
            Map<String, Object> orderMap = listRecords.get(i);
            IcrmLoanCompleteDetails icrmLoanCompleteDetails = buildCompleteDetails(orderMap);
            ;/*IcrmLeadCompleteDetails.builder().amountApproved(loanApplicationStatusDto.getLoanAmountApproved()).approvedBy(loanApplicationStatusDto.getLoanApprovedBy()).loanStatus(loanApplicationStatusDto.getLoanStatus()).externalLoanId(loanApplicationStatusDto.getVendorLoanId()).createdAt(loanApplicationStatusDto.getLoanCreatedAt()).internalLoanId(loanApplicationStatusDto.getLoanId()).loanStatus(loanApplicationStatusDto.getLoanStatus()).loanVendorId(loanApplicationStatusDto.getLoanVendorId())
                    .dateOfInitiation(loanApplicationStatusDto.getLoanCreatedAt()).sanctionAmount(loanApplicationStatusDto.getLoanAmountApproved()).creditLineApprovalDate(loanApplicationStatusDto.getLoanApprovedAt()).updatedAt(loanApplicationStatusDto.getLoanStatusUpdatedAt()).build();*/
             if(userMap.containsKey(icrmLoanCompleteDetails.getSrCompanyId())){
                 icrmLoanCompleteDetails.setCompanyName(userMap.get(icrmLoanCompleteDetails.getSrCompanyId()).getCompanyName());
             }else{
                 User user =userService.getCompanyDetails(icrmLoanCompleteDetails.getSrCompanyId());
                 if(user!=null){
                     userMap.put(icrmLoanCompleteDetails.getSrCompanyId(),user);
                     icrmLoanCompleteDetails.setCompanyName(user.getCompanyName());
                 }
             }
            icrmLoanResponseDto.getCompleteDetails().add(icrmLoanCompleteDetails);
        };
        return icrmLoanResponseDto;
    }


    private  List<Map<String, Object>> getLoanApplicationByCriteria(IcrmLeadRequestDto icrmLeadRequestDto, IcrmLoanResponseDto responseDto) throws CustomException, ParseException {
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
            whereClauseValues.put(loanApplicationStattusPrefix+"created_at->=" , icrmLeadRequestDto.getDateOfDisbursal());
        }

        if(icrmLeadRequestDto.getDateOfDisbursalEnd()!=null){
            whereClauseValues.put(loanApplicationStattusPrefix+"created_at-<=" , icrmLeadRequestDto.getDateOfDisbursalEnd()+ " 23:59:59");
        }

        if(icrmLeadRequestDto.getDateOfSanction()!=null){
            whereClauseValues.put(loanApplicationStattusPrefix+"created_at->=" , icrmLeadRequestDto.getDateOfSanction()+" 00:00:00");
        }

        if(icrmLeadRequestDto.getDateOfSanctionEnd()!=null){
            whereClauseValues.put(loanApplicationStattusPrefix+"created_at-<=" , icrmLeadRequestDto.getDateOfSanctionEnd() +" 23:59:59");
        }

        int offset = (icrmLeadRequestDto.getPageNumber() - 1) * icrmLeadRequestDto.getPageSize();
        int totalNoOfRecords=0;
        int pageSize = icrmLeadRequestDto.getPageSize()==null ? 10: icrmLeadRequestDto.getPageSize();
        if (pageSize < 1) {
            throw new CustomException("Invalid Page Size",HttpStatus.BAD_REQUEST);
        }
        arrSort[0] = loanApplicationPrefix+"created_at desc";
        pageInfo.put(loanApplicationPrefix+"created_to",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date(System.currentTimeMillis()- TimeUnit.MINUTES.toMillis(5))));
        if(icrmLeadRequestDto.getCreatedAtEnd()!=null){
            whereClauseValues.put(loanApplicationPrefix+"created_at-<=" , icrmLeadRequestDto.getCreatedAtEnd()+ " 23:59:59");
        } else {
            whereClauseValues.put(loanApplicationPrefix+"created_at<=", pageInfo.get(loanApplicationPrefix+"created_to"));
        }
        if(icrmLeadRequestDto.getNoOfRecord()==null){
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

        IcrmLoanResponseDto.PaginationInfo paginationInfo = IcrmLoanResponseDto.PaginationInfo.builder().pageSize(pageSize).noOfRecords(totalNoOfRecords).pageNumber(icrmLeadRequestDto.getPageNumber()).noOfPages(noOfPages).build();
        responseDto.setPaginationInfo(paginationInfo);

        return listRecords;

    }


    private IcrmLoanCompleteDetails buildCompleteDetails(Map<String,Object> map){
        IcrmLoanCompleteDetails dto = IcrmLoanCompleteDetails.builder().build();

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



    private String convertLeadToCSVAndEncodeBase64(List<LeadDetailsResponseDto.LeadDetails> leads) {
        try (StringWriter writer = new StringWriter();
             CSVWriter csvWriter = new CSVWriter(writer)) {

            // Write CSV header
            String[] header = {"srCompanyId", "companyName", "brandName", "amount", "duration in months", "status", "loanApplicationId", "tier", "leadSource", "remarks", "loanVendorPartnerId","createdAt","updatedAt"};
            csvWriter.writeNext(header);

            // Write CSV rows
            for (LeadDetailsResponseDto.LeadDetails lead : leads) {
                String[] row = {
                        lead.getSrCompanyId().toString(),
                        lead.getCompanyName(),
                        lead.getBrandName(),
                        lead.getAmount().toString(),
                        lead.getDuration().toString(),
                        lead.getStatus().name(),
                        lead.getLoanApplicationId()!=null?lead.getLoanApplicationId().toString():"",
                        lead.getTier(),
                        lead.getLeadSource(),
                        lead.getRemarks(),
                        lead.getLoanVendorPartnerId()!=null?lead.getLoanVendorPartnerId().toString():"",
                        lead.getCreatedAt().toString(),
                        lead.getUpdatedAt().toString()
                };
                csvWriter.writeNext(row);
            }
            csvWriter.flush(); // Ensure data is flushed to StringWriter

            // Convert CSV to
            // Convert CSV to Base64
            String csvContent = writer.toString();

            return Base64.getEncoder().encodeToString(csvContent.getBytes());

        } catch (Exception e) {
            throw new RuntimeException("Error while converting to CSV and encoding in Base64", e);
        }
    }

    private String convertLoanToCSVAndEncodeBase64(List<IcrmLoanCompleteDetails> loanResponseDtos,Map<Long,String> idNameMap) {
        try (StringWriter writer = new StringWriter();
             CSVWriter csvWriter = new CSVWriter(writer)) {

            // Write CSV header
            String[] header = {"srCompanyId", "companyName", "Internal Loan ID", "Loan Vendor External Loan Id", "Loan Type (RBF/ Preapproved/term loan / credit line)", "Date of initiation", "Loan Vendor Name", "Loan Application Status at Vendor level", "Sanctioned amount", "Disbursed amount"};
            csvWriter.writeNext(header);

            // Write CSV rows
            for (IcrmLoanCompleteDetails loanCompleteDetails : loanResponseDtos) {
                String[] row = {
                        loanCompleteDetails.getSrCompanyId().toString(),
                        loanCompleteDetails.getCompanyName(),
                        loanCompleteDetails.getInternalLoanId()!=null?String.valueOf(loanCompleteDetails.getInternalLoanId()):"",
                        loanCompleteDetails.getExternalLoanId(),
                        loanCompleteDetails.getLoanType(),
                        loanCompleteDetails.getCreatedAt().toString(),
                        idNameMap.get(loanCompleteDetails.getLoanVendorId()),
                        loanCompleteDetails.getExternalLoanStatus(),
                        loanCompleteDetails.getAmountApproved().toString(),
                        String.valueOf(loanCompleteDetails.getDisbursedAmount())
                };
                csvWriter.writeNext(row);
            }
            csvWriter.flush(); // Ensure data is flushed to StringWriter

            // Convert CSV to
            // Convert CSV to Base64
            String csvContent = writer.toString();

            return Base64.getEncoder().encodeToString(csvContent.getBytes());

        } catch (Exception e) {
            throw new RuntimeException("Error while converting to CSV and encoding in Base64", e);
        }
    }



}
