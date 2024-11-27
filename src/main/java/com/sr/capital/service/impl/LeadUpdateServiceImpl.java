package com.sr.capital.service.impl;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.UpdateLeadRequestDto;
import com.sr.capital.dto.request.UserDetails;
import com.sr.capital.dto.response.CreateLeadResponseDto;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BusinessAddressDetails;
import com.sr.capital.entity.mongo.kyc.child.PersonalAddressDetails;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.Pincode;
import com.sr.capital.entity.primary.User;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.kyc.service.DocDetailsService;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.service.UserService;
import com.sr.capital.service.entityimpl.PincodeEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeadUpdateServiceImpl {


    final UserService userService;
    final LoanApplicationRepository loanApplicationRepository;
    final DocDetailsService docDetailsService;
    final AES256 aes256;
    final PincodeEntityServiceImpl pincodeEntityService;
    final CreditPartnerFactoryService creditPartnerFactoryService;
    public Boolean updateLeadDetails(UpdateLeadRequestDto updateLeadRequestDto) throws CustomException {

        CreateLeadResponseDto responseDto = (CreateLeadResponseDto) creditPartnerFactoryService.getPartnerService(updateLeadRequestDto.getLoanVendorName()).updateLead(updateLeadRequestDto.getLoanVendorName(), validateAndBuildRequestDto(RequestData.getTenantId(), updateLeadRequestDto));
        return  true;
    }

    private com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto validateAndBuildRequestDto(String tenantId, UpdateLeadRequestDto updateLeadRequestDto) throws CustomException {

        com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto updateLeadDto =null;
        LoanApplication loanApplication = loanApplicationRepository.findById(updateLeadRequestDto.getLoanId()).orElse(null);
        if(loanApplication!=null) {
            UserDetails user = userService.getCompanyDetailsWithoutEncryption(Long.valueOf(tenantId));

            updateLeadDto = com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto.builder().leadCode(String.valueOf(loanApplication.getExternalLeadCode()))

                    .build();
            log.info("[buildRequestDto] user details {} ", user);
            if (user != null) {
                com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto.LoanDetails loanDetails = com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto.LoanDetails.builder().amount(loanApplication.getLoanAmountRequested()).partnerRefNo(String.valueOf(loanApplication.getId())).termsConditionAcceptance(true)
                        .loanCode(loanApplication.getVendorLoanId()).build();
                updateLeadDto.setLoanApplication(loanDetails);
                List<KycDocDetails<?>> docDetails = docDetailsService.fetchDocDetailsByTenantId(tenantId);

                if (CollectionUtils.isNotEmpty(docDetails)) {

                    for (KycDocDetails<?> doc : docDetails) {
                        if (doc.getDocType() == DocType.PERSONAL_ADDRESS) {
                            log.info("[buildRequestDto] personal doc {} ", doc);
                            validateAndBuildPersonalDetails(doc, user, updateLeadDto, updateLeadRequestDto.getLoanVendorId(), loanApplication.getVendorLoanId());

                        } else if (doc.getDocType() == DocType.BUSINESS_ADDRESS) {
                            log.info("[buildRequestDto] business doc {} ", doc);

                            buildBusinessDetails(doc, updateLeadDto, user,loanApplication.getVendorLoanId());
                        } else if (doc.getDocType() == DocType.BANK_CHEQUE) {

                            log.info("[buildRequestDto] bank cheque {} ", doc);

                        }
                    }


                }

            }
            loanApplication.setLoanStatus(LoanStatus.LEAD_PROCESSING);
            loanApplicationRepository.save(loanApplication);
        }else{
            throw new CustomException("Invalid loan_id",HttpStatus.BAD_REQUEST);
        }
        return updateLeadDto;
    }



    private void buildBusinessDetails(KycDocDetails<?> doc, com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto createLeadRequestDto, UserDetails user,String externalLoanCode) {
        BusinessAddressDetails businessAddressDetails = (BusinessAddressDetails) doc.getDetails();
        com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto.LoanBusiness loanBusiness = com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto.LoanBusiness.builder().loanCode(externalLoanCode).legalStatus(doc.getKycType().getClientType())
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

        buildBusinessPartner(createLeadRequestDto ,businessAddressDetails,externalLoanCode);
        createLeadRequestDto.getLoanApplication().setLoanBusiness(loanBusiness);
    }

    private void buildBusinessPartner(com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto createLeadRequestDto, BusinessAddressDetails businessAddressDetails,String externalLoanCode) {

        List<com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto.LoanBusinessPartner> loanBusinessPartnerList;
        if(CollectionUtils.isNotEmpty(businessAddressDetails.getBusinessPartnerInfo())){
            loanBusinessPartnerList = new ArrayList<>();
            businessAddressDetails.getBusinessPartnerInfo().forEach(businessPartnerInfo -> {

                com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto.LoanBusinessPartner partner =com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto.LoanBusinessPartner.builder().loanCode(externalLoanCode)
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

    private void validateAndBuildPersonalDetails(KycDocDetails<?> doc, UserDetails user, com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto updateLeadRequestDto, Long loanVendorId,String externalLoanCode) throws CustomException {
        updateLeadRequestDto.setFirstName(user.getFirstName());
        updateLeadRequestDto.setMobileNo(user.getMobileNumber());
        updateLeadRequestDto.setEmail(user.getEmail());
        updateLeadRequestDto.setLastName(user.getLastName());
        PersonalAddressDetails personalAddressDetails = (PersonalAddressDetails) doc.getDetails();


        com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto.LoanApplicant loanApplicant = com.sr.capital.external.flexi.dto.request.UpdateLeadRequestDto.LoanApplicant.builder().dob(user.getDateOfBirth())
                .loanCode(externalLoanCode) .panNo(user.getPanNumber()).gender(user.getGender()).isCurrentAccountAvailable(user.getCurrentAccountAvailable()?1:0).build();


        for (PersonalAddressDetails.Address address : personalAddressDetails.getAddress()) {
            if (address.getAddressType() == null || address.getAddressType().equalsIgnoreCase("current")) {
                loanApplicant.setAddressLine1(aes256.decrypt(address.getAddress1()));
                loanApplicant.setAddressLine2(aes256.decrypt(address.getAddress2()));
                loanApplicant.setPincode(aes256.decrypt(address.getPincode()));
                loanApplicant.setOwnershipStatus(address.getOwnershipStatus());

                if(loanApplicant.getAddressLine2()==null){
                    loanApplicant.setAddressLine2("city "+ aes256.decrypt(address.getCity()).concat(" ,state "+aes256.decrypt(address.getState())));
                }else{
                    loanApplicant.setAddressLine2(loanApplicant.getAddressLine2()+" , city "+ aes256.decrypt(address.getCity()).concat(" ,state "+aes256.decrypt(address.getState())));

                }

                Pincode pincode = pincodeEntityService.getPincodeDetailsByVendorId(Long.valueOf(loanApplicant.getPincode()), loanVendorId);
                if (pincode == null) {
                    throw new CustomException("We cannot provide loan at pincode " + loanApplicant.getPincode(), HttpStatus.BAD_REQUEST);
                }
            } else {
            }
        }
        updateLeadRequestDto.getLoanApplication().setLoanApplicant(loanApplicant);
    }
}
