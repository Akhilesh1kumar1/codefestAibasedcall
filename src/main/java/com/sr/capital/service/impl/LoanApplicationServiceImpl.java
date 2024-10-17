package com.sr.capital.service.impl;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.CreateLeadRequestDto;
import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.request.PendingDocumentRequestDto;
import com.sr.capital.dto.response.CreateLeadResponseDto;
import com.sr.capital.dto.response.LoanApplicationResponseDto;
import com.sr.capital.dto.response.LoanApplicationStatusDto;
import com.sr.capital.dto.response.PendingDocumentResponseDto;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BankDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BusinessAddressDetails;
import com.sr.capital.entity.mongo.kyc.child.PersonalAddressDetails;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.User;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.kyc.dto.request.DocDetailsRequest;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.service.DocDetailsService;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.service.LoanApplicationService;
import com.sr.capital.service.LoanOfferService;
import com.sr.capital.service.UserService;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoanApplicationResponseDto submitLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto) throws Exception {

        loanApplicationRequestDto = requestValidationStrategy.validateRequest(loanApplicationRequestDto, RequestType.LOAN_APPLICATION);
        LoanApplication loanApplication = LoanApplication.mapLoanApplication(loanApplicationRequestDto);
        loanApplication = loanApplicationRepository.save(loanApplication);

        LoanApplicationResponseDto loanApplicationResponseDto =LoanApplicationResponseDto.mapLoanApplicationResponse(loanApplication);

        if(loanApplicationRequestDto.getCreateLoanAtVendor()){
            CreateLeadResponseDto responseDto = (CreateLeadResponseDto) creditPartnerFactoryService.getPartnerService(loanApplicationRequestDto.getLoanVendorName()).createLead(loanApplicationRequestDto.getLoanVendorName(),buildRequestDto(RequestData.getTenantId(),loanApplicationResponseDto));
            if(responseDto!=null && responseDto.getStatus()!=null && responseDto.getStatus().equalsIgnoreCase("new")){
                loanApplication.setLoanStatus(LoanStatus.PRE_APPROVED);
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
            List<LoanApplication> loanApplications =loanApplicationRepository.findBySrCompanyId(Long.valueOf(RequestData.getTenantId()));
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
    public PendingDocumentResponseDto fetchPendingDocuments(PendingDocumentRequestDto pendingDocumentRequestDto) throws CustomException {

        LoanApplication loanApplication = loanApplicationRepository.findById(pendingDocumentRequestDto.getLoanId()).orElse(null);
        PendingDocumentResponseDto pendingDocumentResponseDto =null;
        if(loanApplication!=null){
            LoanMetaDataDto loanMetaDataDto =LoanMetaDataDto.builder().loanId(loanApplication.getVendorLoanId().toString()).build();
            loanAllocationService.getLoanVendor(loanMetaDataDto);
            PendingDocumentResponseDto pendingDocumentResponseDtoFromClient = (PendingDocumentResponseDto) creditPartnerFactoryService.getPartnerService(loanMetaDataDto.getLoanVendorName()).getPendingDocuments(loanMetaDataDto);
            if(pendingDocumentResponseDtoFromClient!=null){
                pendingDocumentResponseDto = PendingDocumentResponseDto.builder().pendingList(pendingDocumentResponseDtoFromClient.getPendingList()).build();
            }
        }else{
            throw new CustomException("Invalid LoadId ", HttpStatus.BAD_REQUEST);
        }

        return pendingDocumentResponseDto;
    }


    private CreateLeadRequestDto buildRequestDto(String tenantId,LoanApplicationResponseDto loanApplicationResponseDto) {

        User user = userService.getCompanyDetails(Long.valueOf(tenantId));

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

                docDetails.stream().forEach(doc->{
                    if(doc.getDocType()== DocType.PERSONAL_ADDRESS){
                        log.info("[buildRequestDto] personal doc {} ",doc);

                        buildPersonalDetails(doc,user,createLeadRequestDto);

                    }else if(doc.getDocType() == DocType.BUSINESS_ADDRESS){
                        log.info("[buildRequestDto] business doc {} ",doc);

                        buildBusinessDetails(doc,createLeadRequestDto,user);
                    }else if(doc.getDocType() == DocType.BANK_CHEQUE){

                        log.info("[buildRequestDto] bank cheque {} ",doc);

                        buildAccountDetails(doc,createLeadRequestDto,false);
                    }
                });

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

    private void buildBusinessDetails(KycDocDetails<?> doc, CreateLeadRequestDto createLeadRequestDto,User user) {
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
        createLeadRequestDto.getLoanApplication().setLoanBusiness(loanBusiness);
    }

    private void buildPersonalDetails(KycDocDetails<?> doc, User user, CreateLeadRequestDto createLeadRequestDto) {
        createLeadRequestDto.setFirstName(user.getFirstName());
        createLeadRequestDto.setMobileNumber(user.getMobile());
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
                .panNo(user.getPanNumber()).gender(user.getGender()).build();


        personalAddressDetails.getAddress().forEach(address -> {
            if(address.getAddressType()==null || address.getAddressType().equalsIgnoreCase("current")) {
                loanApplicant.setAddressLine1(aes256.decrypt(address.getAddress1()));
                loanApplicant.setAddressLine2(aes256.decrypt(address.getAddress2()));
                loanApplicant.setPincode(aes256.decrypt(address.getPincode()));
                loanApplicant.setOwnershipStatus(address.getOwnershipStatus());
            }else{
            }
        });
        createLeadRequestDto.getLoanApplication().setLoanApplicant(loanApplicant);
    }
}
