package com.sr.capital.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.*;
import com.sr.capital.dto.response.*;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BankDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BusinessAddressDetails;
import com.sr.capital.entity.mongo.kyc.child.PersonalAddressDetails;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.Pincode;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.kyc.service.DocDetailsService;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.service.LoanApplicationService;
import com.sr.capital.service.LoanOfferService;
import com.sr.capital.service.UserService;
import com.sr.capital.service.entityimpl.PincodeEntityServiceImpl;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanApplicationServiceImpl implements LoanApplicationService {

    final RequestValidationStrategy requestValidationStrategy;
    final LoanApplicationRepository loanApplicationRepository;
    final LoanOfferService loanOfferService;
    final CreditPartnerFactoryService creditPartnerFactoryService;
    final UserService userService;
    final DocDetailsService docDetailsService;
    final AES256 aes256;
    final LoanAllocationServiceImpl loanAllocationService;
    final DocumentSyncHelperServiceImpl documentSyncHelperService;
    final PincodeEntityServiceImpl pincodeEntityService;
    final AppProperties appProperties;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoanApplicationResponseDto submitLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto) throws Exception {

        loanApplicationRequestDto = requestValidationStrategy.validateRequest(loanApplicationRequestDto, RequestType.LOAN_APPLICATION);
        LoanApplication loanApplication =null ;

        if(loanApplicationRequestDto.getLoanId()!=null){
            loanApplicationRequestDto.setLoanStatus(LoanStatus.LEAD_VERIFIED);
            loanApplication = loanApplicationRepository.findById(loanApplicationRequestDto.getLoanId()).orElse(null);
             LoanApplication.mapLoanApplication(loanApplicationRequestDto,loanApplication);
        }else {
            loanApplication = LoanApplication.mapLoanApplication(loanApplicationRequestDto);
            loanApplication = loanApplicationRepository.save(loanApplication);
        }

        LoanApplicationResponseDto loanApplicationResponseDto =LoanApplicationResponseDto.mapLoanApplicationResponse(loanApplication);

        if(loanApplicationRequestDto.getCreateLoanAtVendor()){
            CreateLeadResponseDto responseDto = (CreateLeadResponseDto) creditPartnerFactoryService.getPartnerService(loanApplicationRequestDto.getLoanVendorName()).createLead(loanApplicationRequestDto.getLoanVendorName(), validateAndBuildRequestDto(RequestData.getTenantId(),loanApplicationResponseDto));
            if(responseDto!=null && responseDto.getSuccess()!=null ){
                loanApplication.setLoanStatus(LoanStatus.LEAD_VERIFIED);
                loanApplication.setVendorLoanId(responseDto.getLoanCode());
                loanApplication.setExternalLeadCode(responseDto.getLeadCode());
                loanApplicationRepository.save(loanApplication);

            }
        }
        if(loanApplicationRequestDto.getLoanOfferId()!=null)
           loanOfferService.updateLoanOffer(loanApplicationRequestDto.getLoanOfferId(),true);


        return loanApplicationResponseDto;
    }



    @Override
    public List<LoanApplicationResponseDto> getLoanApplication(UUID loanApplicationId) {

        List<LoanApplicationResponseDto> loanApplicationResponseDtos =new ArrayList<>();

        if(loanApplicationId!=null){
            LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId).orElse(null);
            if(loanApplication!=null){
                loanApplicationResponseDtos.add(LoanApplicationResponseDto.mapLoanApplicationResponse(loanApplication));
            }
        }else{
            List<LoanApplication> loanApplications =loanApplicationRepository.findByCompanyIdOrderByCreatedAtAsc(Long.valueOf(RequestData.getTenantId()));
            if(CollectionUtils.isNotEmpty(loanApplications))
                 loanApplications.forEach(loanApplication -> {
                     loanApplicationResponseDtos.add(LoanApplicationResponseDto.mapLoanApplicationResponse(loanApplication));
                 });
        }
        return loanApplicationResponseDtos;
    }

    @Override
    public List<Object[]> getLoanApplicationStatusByCompanyId(Long srCompanyId) {
        return loanApplicationRepository.findLoanApplicationsWithStatusBySrCompanyId(srCompanyId);

    }

    @Override
    public LoanApplication getLoanApplicationById(UUID loanApplicationId) {
        return loanApplicationRepository.findById(loanApplicationId).orElse(null);
    }

    @Override
    public LoanApplication getLoanApplicationByInternalLoanId(String loanApplicationId) {
        return loanApplicationRepository.findByInternalLoanId(loanApplicationId);

    }


    @Override
    public PendingDocumentResponseDto fetchPendingDocuments(PendingDocumentRequestDto pendingDocumentRequestDto) throws CustomException, IOException {

        LoanApplication loanApplication = loanApplicationRepository.findById(pendingDocumentRequestDto.getLoanId()).orElse(null);
        PendingDocumentResponseDto pendingDocumentResponseDto =null;
        if(loanApplication!=null){
            LoanMetaDataDto loanMetaDataDto =LoanMetaDataDto.builder().loanId(loanApplication.getVendorLoanId().toString()).build();
            loanAllocationService.getLoanVendor(loanMetaDataDto);
            com.sr.capital.external.flexi.dto.response.PendingDocumentResponseDto pendingDocumentResponseDtoFromClient = (com.sr.capital.external.flexi.dto.response.PendingDocumentResponseDto) creditPartnerFactoryService.getPartnerService(loanMetaDataDto.getLoanVendorName()).getPendingDocuments(loanMetaDataDto);
            if(pendingDocumentResponseDtoFromClient!=null && pendingDocumentResponseDtoFromClient.getData()!=null){
                TypeReference<List<PendingDocumentResponseDto.PendingItem>> tRef = new TypeReference<List<PendingDocumentResponseDto.PendingItem>>() {
                };
                List<PendingDocumentResponseDto.PendingItem>  pendingDocumentItems = MapperUtils.convertValue(pendingDocumentResponseDtoFromClient.getData().getPendingList(),tRef);

                if(CollectionUtils.isEmpty(pendingDocumentItems) ){
                    if(loanApplication.getLoanStatus()==LoanStatus.DOCUMENT_UPLOAD){
                        loanApplication.setLoanStatus(LoanStatus.LEAD_PROCESSING);
                        loanApplication.setState(LoanStatus.LEAD_PROCESSING.name());
                        loanApplicationRepository.save(loanApplication);
                    }
                }
                pendingDocumentResponseDto = PendingDocumentResponseDto.builder().pendingList(pendingDocumentItems).build();
            }
        }else{
            throw new CustomException("Invalid LoadId ", HttpStatus.BAD_REQUEST);
        }

        return pendingDocumentResponseDto;
    }

    @Override
    public LoanApplicationResponseDto createLoanAtVendor(CreateLoanAtVendorRequest createLoanAtVendorRequest) throws CustomException {

        LoanApplicationResponseDto loanApplicationResponseDto = null;
        LoanApplication loan =null;
        if(createLoanAtVendorRequest.getLoanId()!=null){
            loan = loanApplicationRepository.findById(createLoanAtVendorRequest.getLoanId()).orElse(null);
            if(loan!=null){
                loanApplicationResponseDto = LoanApplicationResponseDto.mapLoanApplicationResponse(loan);
            }
        }else{
            List<LoanApplication> loanApplications =loanApplicationRepository.findByCompanyIdOrderByCreatedAtAsc(Long.valueOf(RequestData.getTenantId()));
            if(CollectionUtils.isNotEmpty(loanApplications))
                for(LoanApplication loanApplication:loanApplications) {
                    if(loanApplication.getLoanStatus().equals(LoanStatus.LEAD_VERIFIED)) {
                        loan =loanApplication;
                        loanApplicationResponseDto= LoanApplicationResponseDto.mapLoanApplicationResponse(loanApplication);
                        break;
                    }
                };
        }

        if(loanApplicationResponseDto!=null) {
            CreateLeadResponseDto responseDto = (CreateLeadResponseDto) creditPartnerFactoryService.getPartnerService(createLoanAtVendorRequest.getLoanVendorName()).createLead(createLoanAtVendorRequest.getLoanVendorName(), validateAndBuildRequestDto(RequestData.getTenantId(), loanApplicationResponseDto));
            if (responseDto != null && responseDto.getSuccess() != null) {
                loan.setLoanStatus(LoanStatus.LEAD_PROCESSING);
                loan.setVendorLoanId(responseDto.getLoanCode());
                loan.setExternalLeadCode(responseDto.getLeadCode());
                loan.getAuditData().setUpdatedAt(LocalDateTime.now());
                loanApplicationRepository.save(loan);

            }
        }
        return loanApplicationResponseDto;
    }

    @Override
    public SyncDocumentResponseDto syncDocumentToVendor(SyncDocumentToVendor syncDocumentToVendor) throws CustomException {

        LoanApplication loanApplication = loanApplicationRepository.findById(syncDocumentToVendor.getLoanId()).orElse(null);
        if(loanApplication!=null){
            LoanMetaDataDto loanMetaDataDto = LoanMetaDataDto.builder().loanVendorId(loanApplication.getLoanVendorId())
                    .loanId(loanApplication.getVendorLoanId()).srCompanyId(loanApplication.getSrCompanyId()).loanVendorName(syncDocumentToVendor.getLoanVendorName()).build();
            documentSyncHelperService.syncDocumentToVendor(loanMetaDataDto);
            loanApplication.setLoanStatus(LoanStatus.LEAD_PROCESSING);
            loanApplication.getAuditData().setUpdatedAt(LocalDateTime.now());
            loanApplicationRepository.save(loanApplication);
        }

        return SyncDocumentResponseDto.builder().loanId(syncDocumentToVendor.getLoanId()).build();
    }

    @Override
    public LoanApplication updateLoanApplication(LoanApplication loanApplication) {
        return loanApplicationRepository.save(loanApplication);
    }

    @Override
    public EnachRedirectionUrlResponseDto getRedirectionurl(EnachRedirectUrlRequestDto enachRedirectUrlRequestDto) {

        LoanApplication loanApplication = loanApplicationRepository.findById(enachRedirectUrlRequestDto.getLoanId()).orElse(null);

        if(loanApplication!=null){
               if(enachRedirectUrlRequestDto.getRedirectionUrl()==null){
                   enachRedirectUrlRequestDto.setRedirectionUrl(String.format(appProperties.getFlexiRedirectUrl(), loanApplication.getVendorLoanId()));
               }
            return EnachRedirectionUrlResponseDto.builder().loanId(enachRedirectUrlRequestDto.getLoanId()).redirectUrl(enachRedirectUrlRequestDto.getRedirectionUrl()).build();
        }else {
            return null;
        }
    }


    private CreateLeadRequestDto validateAndBuildRequestDto(String tenantId, LoanApplicationResponseDto loanApplicationResponseDto) throws CustomException {

        UserDetails user = userService.getCompanyDetailsWithoutEncryption(Long.valueOf(tenantId));

        CreateLeadRequestDto createLeadRequestDto = CreateLeadRequestDto.builder().clientLoanId(String.valueOf(loanApplicationResponseDto.getId()))
                .customerCategory("self_employed").
                applicationId(String.valueOf(loanApplicationResponseDto.getId())).
                clientCustomerId(tenantId).
                tenure(loanApplicationResponseDto.getLoanDuration()).
               // numberOfRepayments(loanApplicationResponseDto.getLoanDuration()).
                principalAmount(loanApplicationResponseDto.getLoanAmountRequested())
                .build();
       log.info("[buildRequestDto] user details {} ",user);
        if(user!=null){
             CreateLeadRequestDto.LoanDetails loanDetails =CreateLeadRequestDto.LoanDetails.builder().amount(loanApplicationResponseDto.getLoanAmountRequested()).partnerRefNo(String.valueOf(loanApplicationResponseDto.getId())).termsConditionAcceptance(true).build();
             createLeadRequestDto.setLoanApplication(loanDetails);
             List<KycDocDetails<?>> docDetails =docDetailsService.fetchDocDetailsByTenantId(tenantId);

            if(CollectionUtils.isNotEmpty(docDetails)){

                for (KycDocDetails<?> doc : docDetails) {
                    if (doc.getDocType() == DocType.PERSONAL_ADDRESS) {
                        log.info("[buildRequestDto] personal doc {} ", doc);
                        validateAndBuildPersonalDetails(doc, user, createLeadRequestDto, loanApplicationResponseDto.getLoanVendorId());

                    } else if (doc.getDocType() == DocType.BUSINESS_ADDRESS) {
                        log.info("[buildRequestDto] business doc {} ", doc);

                        buildBusinessDetails(doc, createLeadRequestDto, user);
                    } else if (doc.getDocType() == DocType.BANK_CHEQUE) {

                        log.info("[buildRequestDto] bank cheque {} ", doc);

                        buildAccountDetails(doc, createLeadRequestDto, false);
                    }
                }

                if(CollectionUtils.isEmpty(createLeadRequestDto.getDisbursementAccounts())){
                    buildAccountDetails(null,createLeadRequestDto,true);
                }
            }

        }

        return createLeadRequestDto;
    }

    private void buildAccountDetails(KycDocDetails<?> doc, CreateLeadRequestDto createLeadRequestDto,Boolean isDummy) {
        List<CreateLeadRequestDto.DisbursementAccount> disbursementAccounts = new ArrayList<>();

        if(!isDummy) {

           List<BankDocDetails> bankDocDetailsList = (List<BankDocDetails>) doc.getDetails();

           bankDocDetailsList.forEach(bankDocDetails -> {
               CreateLeadRequestDto.DisbursementAccount disbursementAccount = CreateLeadRequestDto.DisbursementAccount.builder()
                       .accountName(aes256.decrypt(bankDocDetails.getAccountName()))
                       .accountNo(aes256.decrypt(bankDocDetails.getAccountNo()))
                       .bankName(bankDocDetails.getBankName()).bankBranchName(aes256.decrypt(bankDocDetails.getBankAddress()))
                       .ifscCode(bankDocDetails.getIfscCode()).bankAccountType(bankDocDetails.getBankAccountType()).amount(createLeadRequestDto.getPrincipalAmount())
                       .build();
               disbursementAccounts.add(disbursementAccount);
           });
       }else{
            CreateLeadRequestDto.DisbursementAccount disbursementAccount = CreateLeadRequestDto.DisbursementAccount.builder()
                    .accountName("DUMMY")
                    .accountNo("1234567890")
                    .bankName("DUMMY").bankBranchName("DUMMY")
                    .ifscCode("ABCD0000123").bankAccountType("saving").amount(createLeadRequestDto.getPrincipalAmount())
                    .build();
            disbursementAccounts.add(disbursementAccount);

       }
        createLeadRequestDto.setDisbursementAccounts(disbursementAccounts);
    }

    private void buildBusinessDetails(KycDocDetails<?> doc, CreateLeadRequestDto createLeadRequestDto,UserDetails user) {
        BusinessAddressDetails businessAddressDetails = (BusinessAddressDetails) doc.getDetails();
        CreateLeadRequestDto.LoanBusiness loanBusiness = CreateLeadRequestDto.LoanBusiness.builder().legalStatus(doc.getKycType().getClientType())
                .addressLine1(aes256.decrypt(businessAddressDetails.getAddress1()))
                .addressLine2(aes256.decrypt(businessAddressDetails.getAddress2()))
                .ownershipStatus(businessAddressDetails.getBusinessOwnerShipStatus())
                .pincode(aes256.decrypt(businessAddressDetails.getPincode()))
                .partnerCount(businessAddressDetails.getNoOfDirector()).ownershipStatus(businessAddressDetails.getBusinessOwnerShipStatus())
                .hasGstRegistration(businessAddressDetails.getGstRegistered()==true?1:0).build();
        /*CreateLeadRequestDto.Business business = CreateLeadRequestDto.Business.builder()
                .businessPanNumber(aes256.decrypt(businessAddressDetails.getBusinessPanNumber()))
                .businessType(businessAddressDetails.getBusinessType()).nameOfBusiness(businessAddressDetails.getBusinessName())
                .businessRegisteredOfficePincode(Long.valueOf(aes256.decrypt(businessAddressDetails.getPincode())))
                .businessRegisteredOfficeState(aes256.decrypt(businessAddressDetails.getState()))
                .sectorType(businessAddressDetails.getSectorType())
                .businessRegisteredOfficeAddress(aes256.decrypt(businessAddressDetails.getAddress())).businessPhoneNumber(phoneNumber)
                .typeOfConstitution(doc.getKycType().name()).industryType(businessAddressDetails.getIndustryType())
                .build();*/

        buildBusinessPartner(createLeadRequestDto ,businessAddressDetails);
        createLeadRequestDto.getLoanApplication().setLoanBusiness(loanBusiness);
    }

    private void buildBusinessPartner(CreateLeadRequestDto createLeadRequestDto, BusinessAddressDetails businessAddressDetails) {

        List<CreateLeadRequestDto.LoanBusinessPartner> loanBusinessPartnerList;
        if(CollectionUtils.isNotEmpty(businessAddressDetails.getBusinessPartnerInfo())){
            loanBusinessPartnerList = new ArrayList<>();
            businessAddressDetails.getBusinessPartnerInfo().forEach(businessPartnerInfo -> {

                CreateLeadRequestDto.LoanBusinessPartner partner =CreateLeadRequestDto.LoanBusinessPartner.builder()
                .dob(aes256.decrypt(businessPartnerInfo.getDob())).
                address(aes256.decrypt(businessPartnerInfo.getAddress())).
                name(aes256.decrypt(businessPartnerInfo.getName())).
                gender(businessPartnerInfo.getGender()).city(businessPartnerInfo.getCity()).state(businessPartnerInfo.getState()).
                mobileNo(aes256.decrypt(businessPartnerInfo.getMobileNumber())).
                pincode(aes256.decrypt(businessPartnerInfo.getPincode())).
                panNo(aes256.decrypt(businessPartnerInfo.getPanNumber())).
                holding(Double.valueOf(aes256.decrypt(businessPartnerInfo.getBusinessPartnerHolding()))).interimBusinessPartnerIdentifier(businessPartnerInfo.getUniqueIdentifier())
                        .build();
                loanBusinessPartnerList.add(partner);
            });
        } else {
            loanBusinessPartnerList = null;
        }
        createLeadRequestDto.getLoanApplication().setLoanBusinessPartners(loanBusinessPartnerList);
    }

    private void validateAndBuildPersonalDetails(KycDocDetails<?> doc, UserDetails user, CreateLeadRequestDto createLeadRequestDto,Long loanVendorId) throws CustomException {
        createLeadRequestDto.setFirstName(user.getFirstName());
        createLeadRequestDto.setMobileNumber(user.getMobileNumber());
        createLeadRequestDto.setEmail(user.getEmail());
        createLeadRequestDto.setLastName(user.getLastName());
        createLeadRequestDto.setFatherName(user.getFatherName());
        createLeadRequestDto.setDateOfBirth(user.getDateOfBirth());
        createLeadRequestDto.setPanNumber(user.getPanNumber());
        PersonalAddressDetails personalAddressDetails = (PersonalAddressDetails) doc.getDetails();
        createLeadRequestDto.setCategory("unsecured");
        createLeadRequestDto.setSubCategory("fresh");
        createLeadRequestDto.setGender(user.getGender());

        CreateLeadRequestDto.LoanApplicant loanApplicant = CreateLeadRequestDto.LoanApplicant.builder().dob(user.getDateOfBirth())
                .panNo(user.getPanNumber()).gender(user.getGender()).isCurrentAccountAvailable(user.getCurrentAccountAvailable()?1:0).build();


        for (PersonalAddressDetails.Address address : personalAddressDetails.getAddress()) {
            if (address.getAddressType() == null || address.getAddressType().equalsIgnoreCase("current")) {
                loanApplicant.setAddressLine1(aes256.decrypt(address.getAddress1()));
                loanApplicant.setAddressLine2(aes256.decrypt(address.getAddress2()));

                if(loanApplicant.getAddressLine2()==null){
                    loanApplicant.setAddressLine2("city "+ aes256.decrypt(address.getCity()).concat(" ,state "+aes256.decrypt(address.getState())));
                }else{
                    loanApplicant.setAddressLine2(loanApplicant.getAddressLine2()+" , city "+ aes256.decrypt(address.getCity()).concat(" ,state "+aes256.decrypt(address.getState())));

                }

                loanApplicant.setPincode(aes256.decrypt(address.getPincode()));
                loanApplicant.setOwnershipStatus(address.getOwnershipStatus());
                Pincode pincode = pincodeEntityService.getPincodeDetailsByVendorId(Long.valueOf(loanApplicant.getPincode()), loanVendorId);
                if (pincode == null) {
                    throw new CustomException("We cannot provide loan at pincode " + loanApplicant.getPincode(), HttpStatus.BAD_REQUEST);
                }
            } else {
            }
        }
        createLeadRequestDto.getLoanApplication().setLoanApplicant(loanApplicant);
    }



}
