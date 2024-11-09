package com.sr.capital.service.impl;

import com.sr.capital.dto.request.AcceptSanctionOfferDto;
import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.response.SanctionDto;
import com.sr.capital.entity.mongo.SanctionDetails;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.LoanApplicationStatus;
import com.sr.capital.external.flexi.dto.response.AcceptSanctionOffer;
import com.sr.capital.external.flexi.dto.response.SanctionResponseDto;
import com.sr.capital.external.flexi.enums.FlexiStatus;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.repository.mongo.SanctionRepository;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.service.entityimpl.LoanApplicationStatusEntityServiceImpl;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SanctionServiceImpl {

    final LoanApplicationStatusEntityServiceImpl loanApplicationStatusEntityService;
    final CreditPartnerFactoryService creditPartnerFactoryService;
    final SanctionRepository sanctionRepository;
    final LoanApplicationRepository loanApplicationRepository;

    public SanctionDto getSanctionDetails(UUID loanApplicationId,String loanVendorName){
        LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId).orElse(null);
        if(loanApplication!=null){
            LoanMetaDataDto loanMetaDataDto =LoanMetaDataDto.builder().srCompanyId(loanApplication.getSrCompanyId()).loanVendorId(loanApplication.getLoanVendorId())
                    .loanId(loanApplication.getVendorLoanId()).internalLoanId(loanApplicationId).loanVendorName(loanVendorName).build();
            SanctionDto sanctionDto = fetchAndSaveSanctionDetails(loanMetaDataDto);
            if(sanctionDto!=null){
                if(loanApplication.getLoanStatus().name().equalsIgnoreCase(LoanStatus.LEAD_PROCESSING.name()){
                    loanApplication.setLoanStatus(LoanStatus.LOAN_GENERATE);
                    loanApplication.setState(LoanStatus.LOAN_GENERATE.name());
                    loanApplicationRepository.save(loanApplication);
                }
            }
            return sanctionDto;
        }
        return null;
    }

    public Boolean acceptOffer(AcceptSanctionOfferDto acceptSanctionOffer){

        LoanApplication loanApplication = loanApplicationRepository.findById(acceptSanctionOffer.getLoanApplicationId()).orElse(null);
        if(loanApplication!=null){

            LoanApplicationStatus loanApplicationStatus = loanApplicationStatusEntityService.getLoanApplicationStatusByLoanId(loanApplication.getId());
            if(loanApplicationStatus!=null) {

                LoanMetaDataDto loanMetaDataDto = LoanMetaDataDto.builder().srCompanyId(loanApplication.getSrCompanyId()).loanVendorId(loanApplication.getLoanVendorId())
                        .loanId(loanApplication.getVendorLoanId()).internalLoanId(acceptSanctionOffer.getLoanApplicationId()).loanVendorName(acceptSanctionOffer.getLoanVendorName()).sanctionCode(loanApplicationStatus.getSanctionCode()).build();

                if(acceptSanctionOffer.getAcceptOffer()) {

                    AcceptSanctionOffer acceptSanctionOffer1 = (AcceptSanctionOffer) creditPartnerFactoryService.getPartnerService(loanMetaDataDto.getLoanVendorName()).acceptOffer(loanMetaDataDto);

                    loanApplication.setLoanStatus(LoanStatus.LOAN_VERIFICATION);

                    loanApplicationStatus.setVendorStatus(LoanStatus.APPROVED.name());
                }else{
                    loanApplication.setLoanStatus(LoanStatus.LOAN_DECLINE);

                }
                loanApplicationRepository.save(loanApplication);

                loanApplicationStatusEntityService.saveLoanApplicationStatus(loanApplicationStatus);
            }
        }
        return true;
    }

    private SanctionDto fetchAndSaveSanctionDetails(LoanMetaDataDto loanMetaDataDto){
        SanctionDto sanctionDto =null;
        try {
            LoanApplicationStatus loanApplicationStatus = loanApplicationStatusEntityService.getLoanApplicationStatusByLoanId(loanMetaDataDto.getInternalLoanId());
            if(loanApplicationStatus==null) {
                sanctionDto = fetchAndSaveSanctionDetailsFromPartner(loanMetaDataDto);
            }else{
                SanctionDetails sanctionDetails = sanctionRepository.findBySrCompanyIdAndVendorSanctionCode(loanMetaDataDto.getSrCompanyId(), loanApplicationStatus.getSanctionCode());
                if(sanctionDetails!=null){
                    SanctionDto.PartnerIntegrationProject partnerIntegrationProject = MapperUtils.convertValue(sanctionDetails.getSanctionDetails(),SanctionDto.PartnerIntegrationProject.class);
                    sanctionDto =SanctionDto.builder().partnerIntegrationProject(partnerIntegrationProject).build();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(sanctionDto!=null){
            loanMetaDataDto.setVendorStatus(FlexiStatus.APPROVED.name());
            loanMetaDataDto.setSanctionDto(sanctionDto);
            buildLoanApplicationStatus(loanMetaDataDto);

        }

        return sanctionDto;
    }

    private SanctionDto fetchAndSaveSanctionDetailsFromPartner(LoanMetaDataDto loanMetaDataDto) throws IOException {

        SanctionResponseDto sanctionResponseDto = (SanctionResponseDto) creditPartnerFactoryService.getPartnerService(loanMetaDataDto.getLoanVendorName()).fetchSanctionDetails(loanMetaDataDto);

       if(sanctionResponseDto!=null && sanctionResponseDto.getSuccess()) {
           SanctionDetails sanctionDetails =SanctionDetails.builder().sanctionDetails(sanctionResponseDto.getData())
           .srCompanyId(loanMetaDataDto.getSrCompanyId()).vendorSanctionCode(sanctionResponseDto.getData().getPartnerIntegrationProject().getSanction_code()).loanId(loanMetaDataDto.getInternalLoanId()).build();
           sanctionRepository.save(sanctionDetails);
           SanctionDto.PartnerIntegrationProject partnerIntegrationProject = MapperUtils.convertValue(sanctionResponseDto.getData().getPartnerIntegrationProject(),SanctionDto.PartnerIntegrationProject.class);
           return SanctionDto.builder().partnerIntegrationProject(partnerIntegrationProject).postSanctionConditionsArray(sanctionResponseDto.getData().getPostSanctionConditionsArray()).build();
       }
       return null;
    }

    private LoanApplicationStatus buildLoanApplicationStatus(LoanMetaDataDto loanMetaDataDto) {

            /*BigDecimal totalDisbursementAmount = BigDecimal.ZERO;
            loanStatusUpdateWebhookDto.getDisbursementAccounts().forEach(disbursementAccount -> {
                if (disbursementAccount.getDisbursementId() != null && disbursementAccount.getDisbursedAmount()!=null) {
                    totalDisbursementAmount.add(BigDecimal.valueOf(disbursementAccount.getDisbursedAmount()));
                }
            });*/
          LoanApplicationStatus  loanApplicationStatus = LoanApplicationStatus.builder().loanId(loanMetaDataDto.getInternalLoanId())
                    .vendorLoanId(loanMetaDataDto.getLoanId()).vendorStatus(loanMetaDataDto.getVendorStatus())
                    .comment("Loan Sanctioned")
                    .loanAmountApproved(BigDecimal.valueOf(Long.parseLong(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getLoanAmount().replaceAll(",",""))))
                    .interestRate(Double.valueOf(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getAnnualRateOfInterest().replaceAll("%","")))
                    .interestAmountAtSanction(BigDecimal.valueOf(Long.parseLong(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getTotalInterest().replaceAll(",",""))))
                    .loanDuration(Integer.valueOf(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getLoanRepaymentPeriod())).
                    totalDisbursedAmount(BigDecimal.valueOf(Long.parseLong(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getDisbursalAmount().replaceAll(",",""))))
                    .sanctionCode(loanMetaDataDto.getSanctionCode()).build();
            loanApplicationStatusEntityService.saveLoanApplicationStatus(loanApplicationStatus);

        return loanApplicationStatus;
    }

}
