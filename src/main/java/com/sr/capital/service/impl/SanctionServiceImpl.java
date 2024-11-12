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
import java.util.Arrays;
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

            if(1==1){
                return buildDummyDto(loanMetaDataDto);
            }

            SanctionDto sanctionDto = fetchAndSaveSanctionDetails(loanMetaDataDto);
            if(sanctionDto!=null){
                if(loanApplication.getLoanStatus().name().equalsIgnoreCase(LoanStatus.LEAD_PROCESSING.name())){
                    loanApplication.setLoanStatus(LoanStatus.LOAN_GENERATE);
                    loanApplication.setState(LoanStatus.LOAN_GENERATE.name());
                    loanApplicationRepository.save(loanApplication);
                }
            }
            return sanctionDto;
        }
        return null;
    }

    private SanctionDto buildDummyDto(LoanMetaDataDto loanMetaDataDto) {
        SanctionDto.PartnerIntegrationProject partnerIntegrationProject = SanctionDto.PartnerIntegrationProject.builder()
                .tnc(Arrays.asList(
                        "*All government taxes, wherever applicable, will be applied on the above charges.",
                        "**Pre-EMI Interest - Interest on disbursed amount from disbursement date to billing date i.e. 1st or 16th of the month."
                ))
                .product("Term Loan Fixed APR")
                .sanction_code("NjM3ZGIxZDNkeHhoeXx8MTExMTExMTExMTF8fDQwNTlhMTFsYm5nYnpqZw==")
                .totalGst("1260")
                .emiAmount("17,639")
                .feeCharges(Arrays.asList(
                        SanctionDto.FeeCharge.builder().key("PROCESSING_FEE").raw("5900").index(1)
                                .label("Processing Fee (including GST @18%)").value("5,900").build(),
                        SanctionDto.FeeCharge.builder().key("INSURANCE_PREMIUM").raw("0").index(5)
                                .label("Insurance Premium").value("0").build(),
                        SanctionDto.FeeCharge.builder().key("PRE_EMI_INTEREST").raw("3308").index(4)
                                .label("Pre EMI Interest**").value("3,308").build(),
                        SanctionDto.FeeCharge.builder().key("STAMP_DUTY").raw("250").index(3)
                                .label("Stamp Duty").value("250").build(),
                        SanctionDto.FeeCharge.builder().key("DOCUMENTATION_CHARGES").raw("2360").index(2)
                                .label("Documentation Charges (including GST @18%)").value("2,360")
                                .sub_label("(including GST @18%)").build()
                ))
                .loanAmount("2,50,000")
                .totalInterest("67,500")
                .totalRepayable("3,17,500")
                .disbursalAmount("2,38,182")
                .processingFeeOg("5000")
                .totalFeeCharges("11,818")
                .detailsOfCharges(Arrays.asList(
                        SanctionDto.DetailsOfCharge.builder().label("Cheque Bounce Charges").value("Rs. 750/- for each bounce").build(),
                        SanctionDto.DetailsOfCharge.builder().label("Penal Charges").value("4% per month on overdue amount").build(),
                        SanctionDto.DetailsOfCharge.builder().label("Loan Pre-payment Charges").value("0% of principal outstanding on amount foreclosed").build()
                ))
                .processingFeeGst("900")
                .documentChargesOg("2000")
                .interestFrequency("year%")
                .documentChargesGst("360")
                .loanRepaymentPeriod("18")
                .annualRateOfInterest("32%")
                .monthlyRateOfInterest("2.6469%")
                .loanRepaymentFrequency("Months")
                .postSanctionConditions("Escrow not required,Repayment to be taken from HDFC Bank CURRENT A/C - 50200035716144,Required CPV of office: shop no 11 Near sahyog society Opp Omkar Plywood Sayan Surat Hyderabad Telangana 500085,One reference of relative and one reference of buyer/supplier required,Zero Foreclosure Charges Post 6 months,Required ownership proof of residential address and business address.")
                .build();

        // Creating SanctionDto with partnerIntegrationProject and postSanctionConditionsArray
        SanctionDto sanctionDto = SanctionDto.builder().partnerIntegrationProject(partnerIntegrationProject).postSanctionConditionsArray(Arrays.asList(
                "One reference of relative and one reference of buyer/supplier required",
                "Repayment to be taken from HDFC Bank CURRENT A/C - 50200035716144",
                "Required CPV of office: shop no 11 Near sahyog society Opp Omkar Plywood Sayan Surat Hyderabad Telangana 500085",
                "Required ownership proof of residential address and business address.",
                "Zero Foreclosure Charges Post 6 months",
                "Escrow not required"
        )).build();

        return sanctionDto;
    }

    public Boolean acceptOffer(AcceptSanctionOfferDto acceptSanctionOffer){

        LoanApplication loanApplication = loanApplicationRepository.findById(acceptSanctionOffer.getLoanId()).orElse(null);
        if(loanApplication!=null){

            LoanApplicationStatus loanApplicationStatus = loanApplicationStatusEntityService.getLoanApplicationStatusByLoanId(loanApplication.getId());
            if(loanApplicationStatus!=null) {

                LoanMetaDataDto loanMetaDataDto = LoanMetaDataDto.builder().srCompanyId(loanApplication.getSrCompanyId()).loanVendorId(loanApplication.getLoanVendorId())
                        .loanId(loanApplication.getVendorLoanId()).internalLoanId(acceptSanctionOffer.getLoanId()).loanVendorName(acceptSanctionOffer.getLoanVendorName()).sanctionCode(loanApplicationStatus.getSanctionCode()).build();

                if(acceptSanctionOffer.getAcceptOffer()) {

                  //  AcceptSanctionOffer acceptSanctionOffer1 = (AcceptSanctionOffer) creditPartnerFactoryService.getPartnerService(loanMetaDataDto.getLoanVendorName()).acceptOffer(loanMetaDataDto);

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
