package com.sr.capital.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.response.DisbursementDetailsResponseDto;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.LoanApplicationStatus;
import com.sr.capital.entity.primary.LoanDisbursed;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.flexi.dto.response.DisbursmentDetails;
import com.sr.capital.repository.mongo.DisbursmentMongoRepository;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.service.entityimpl.LoanApplicationStatusEntityServiceImpl;
import com.sr.capital.service.entityimpl.LoanDistributionEntityServiceImpl;
import com.sr.capital.util.CoreUtil;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisbursementServiceImpl {


    final LoanApplicationStatusEntityServiceImpl loanApplicationStatusEntityService;
    final LoanApplicationRepository loanApplicationRepository;
    final LoanDistributionEntityServiceImpl loanDistributionService;
    final DisbursmentMongoRepository disbursmentMongoRepository;
    final CreditPartnerFactoryService creditPartnerFactoryService;

    static TypeReference<List<DisbursementDetailsResponseDto>> tRef = new TypeReference<List<DisbursementDetailsResponseDto>>() {
    };

    public List<DisbursementDetailsResponseDto> getDisbursmentDetails(UUID loanApplicationId, String loanVendorName) throws IOException, CustomException {
        LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId).orElse(null);
        if (loanApplication != null) {
            LoanMetaDataDto loanMetaDataDto = LoanMetaDataDto.builder().srCompanyId(loanApplication.getSrCompanyId()).loanVendorId(loanApplication.getLoanVendorId())
                    .loanId(loanApplication.getVendorLoanId()).internalLoanId(loanApplicationId).loanVendorName(loanVendorName).build();
            return fetchAndSaveDisbursementDetails(loanMetaDataDto);
        }
        return null;
    }
    private List<DisbursementDetailsResponseDto> fetchAndSaveDisbursementDetails(LoanMetaDataDto loanMetaDataDto) throws IOException, CustomException {
        List<DisbursementDetailsResponseDto> disbursementDetailResponseDtos =new ArrayList<>();

        LoanApplicationStatus loanApplicationStatus = loanApplicationStatusEntityService.getLoanApplicationStatusByLoanId(loanMetaDataDto.getInternalLoanId());
        Boolean disbursmentDetailsFound =false;
        if(loanApplicationStatus!=null) {

            if(loanMetaDataDto.getVendorDisbursedId()==null) {
                List<LoanDisbursed> loanDisburseds = loanDistributionService.getLoanDisbursedDetailsByStatusId(loanApplicationStatus.getId());
                if(CollectionUtils.isNotEmpty(loanDisburseds)) {
                    log.info("Disbursement details found ");
                    disbursmentDetailsFound =true;
                }
            }else{
                List<LoanDisbursed> loanDisburseds = loanDistributionService.getLoanDisbursedDetailsByStatusId(loanApplicationStatus.getId());
                if(CollectionUtils.isNotEmpty(loanDisburseds)){
                    log.info("Disbursement details found ");
                    disbursmentDetailsFound =true;
                }
            }
         }

        if(!disbursmentDetailsFound){
            DisbursementDetailsResponseDto disbursementDetailsResponseDto1 = fetchAndSaveDisbursementDetailsFromPartner(loanMetaDataDto, loanApplicationStatus.getId());
            disbursementDetailResponseDtos.add(disbursementDetailsResponseDto1);
        }else{
            com.sr.capital.entity.mongo.DisbursementDetails disbursementDetails1 = disbursmentMongoRepository.findBySrCompanyIdAndLoanId(loanMetaDataDto.getSrCompanyId(), loanMetaDataDto.getInternalLoanId());

            disbursementDetailResponseDtos = MapperUtils.convertValue( disbursementDetails1.getDisbursementDetails(),tRef);
        }

        return disbursementDetailResponseDtos;
    }


    private DisbursementDetailsResponseDto fetchAndSaveDisbursementDetailsFromPartner(LoanMetaDataDto loanMetaDataDto, Long statusId) throws IOException, CustomException {

        DisbursmentDetails disbursementDetails = (DisbursmentDetails) creditPartnerFactoryService.getPartnerService(loanMetaDataDto.getLoanVendorName()).fetchDisburmentDetails(loanMetaDataDto);
        DisbursementDetailsResponseDto disbursementDetailsResponseDto1 =null;
        if(disbursementDetails!=null ) {
             disbursementDetailsResponseDto1 = MapperUtils.convertValue(disbursementDetails, DisbursementDetailsResponseDto.class);
            if(disbursementDetailsResponseDto1 !=null){
                loanMetaDataDto.setDisbursementDetailsResponseDto(disbursementDetailsResponseDto1);
                saveDisbursementDetails(loanMetaDataDto, statusId);
            }
        }
        return disbursementDetailsResponseDto1;
    }
    private void saveDisbursementDetails(LoanMetaDataDto loanMetaDataDto, Long statusId) {

        if(loanMetaDataDto.getDisbursementDetailsResponseDto()!=null){
            List<Object> disbursementDetailsList =new ArrayList<>();
            DisbursementDetailsResponseDto disbursementDetailsResponseDto =loanMetaDataDto.getDisbursementDetailsResponseDto();
           LoanDisbursed loanDisbursed = LoanDisbursed.builder()
                   .loanAmountDisbursed(BigDecimal.valueOf(disbursementDetailsResponseDto.getDisbursementAmount()))
                   .loanApplicationStatusId(loanMetaDataDto.getLoanApplicationStatusId())
                   .durationAtDisbursal(disbursementDetailsResponseDto.getRepaymentPeriod())
                   .interestRateAtDisbursal(disbursementDetailsResponseDto.getInterestRate())
                   .interestAmountAtDisbursal(BigDecimal.valueOf(disbursementDetailsResponseDto.getApprovedAmount()))
                   .vendorDisbursedId(disbursementDetailsResponseDto.getUtrNo())
                   .disbursedDate(disbursementDetailsResponseDto.getDisbursalDate() != null ?
                           CoreUtil.convertTOdate(disbursementDetailsResponseDto.getDisbursalDate(),"yyyy-MM-dd") : null)
                   .loanId(loanMetaDataDto.getInternalLoanId())
                   .vendorDisbursedId(disbursementDetailsResponseDto.getUtrNo()==null?
                           disbursementDetailsResponseDto.getLoanCode(): disbursementDetailsResponseDto.getUtrNo())
                   .emiAmount(disbursementDetailsResponseDto.getEmiAmount() != null ?
                           BigDecimal.valueOf(disbursementDetailsResponseDto.getEmiAmount()) : null)
                   .loanApplicationStatusId(statusId)
                   .build();
            loanDistributionService.saveLoanDisbursed(loanDisbursed);

            disbursementDetailsList.add(disbursementDetailsResponseDto);
            com.sr.capital.entity.mongo.DisbursementDetails disbursementDetails1 = com.sr.capital.entity.mongo.DisbursementDetails.builder()
                    .srCompanyId(loanMetaDataDto.getSrCompanyId()).loanId(loanMetaDataDto.getInternalLoanId())
                    .disbursementDetails(disbursementDetailsList).build();
            disbursmentMongoRepository.save(disbursementDetails1);
        }

    }





}
