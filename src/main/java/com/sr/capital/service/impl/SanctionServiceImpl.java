package com.sr.capital.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.UpdateSanctionOfferDto;
import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.response.SanctionDto;
import com.sr.capital.entity.mongo.SanctionDetails;
import com.sr.capital.entity.mongo.kyc.child.SanctionMetaDataDetails;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.LoanApplicationStatus;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.flexi.dto.response.AcceptSanctionOffer;
import com.sr.capital.external.flexi.dto.response.KfsResponseDto;
import com.sr.capital.external.flexi.dto.response.SanctionResponseDto;
import com.sr.capital.external.flexi.enums.FlexiStatus;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.repository.mongo.SanctionRepository;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.service.entityimpl.LoanApplicationStatusEntityServiceImpl;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class SanctionServiceImpl {

    final LoanApplicationStatusEntityServiceImpl loanApplicationStatusEntityService;
    final CreditPartnerFactoryService creditPartnerFactoryService;
    final SanctionRepository sanctionRepository;
    final LoanApplicationRepository loanApplicationRepository;

    public SanctionDto getSanctionDetails(UUID loanApplicationId,String loanVendorName) throws CustomException {
        LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId).orElse(null);
        if(loanApplication!=null){
            LoanMetaDataDto loanMetaDataDto =LoanMetaDataDto.builder().srCompanyId(loanApplication.getSrCompanyId()).loanVendorId(loanApplication.getLoanVendorId())
                    .loanId(loanApplication.getVendorLoanId()).internalLoanId(loanApplicationId).loanVendorName(loanVendorName).build();

//            if(1==2){
//                return buildDummyDto(loanMetaDataDto);
//            }

            SanctionDto sanctionDto = fetchAndSaveSanctionDetails(loanMetaDataDto);
            if(sanctionDto!=null){
                if(loanApplication.getLoanStatus().name().equalsIgnoreCase(LoanStatus.LEAD_PROCESSING.name())){
                    loanApplication.setLoanStatus(LoanStatus.LOAN_GENERATE);
                    loanApplication.setState(LoanStatus.LOAN_GENERATE.name());
                    loanApplication.getAuditData().setUpdatedAt(LocalDateTime.now());
                    loanApplication.getAuditData().setUpdatedBy(String.valueOf(RequestData.getUserId()));
                    loanApplicationRepository.save(loanApplication);
                }
            }
            return sanctionDto;
        }
        return null;
    }

    public KfsResponseDto getKfsDetails(UUID loanApplicationId,String loanVendorName) throws CustomException {

        LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId).orElse(null);
        KfsResponseDto kfsResponseDto =null;
        if(loanApplication!=null) {
            LoanMetaDataDto loanMetaDataDto = LoanMetaDataDto.builder().srCompanyId(loanApplication.getSrCompanyId()).loanVendorId(loanApplication.getLoanVendorId())
                    .loanId(loanApplication.getVendorLoanId()).internalLoanId(loanApplicationId).loanVendorName(loanVendorName).build();

             kfsResponseDto = (KfsResponseDto) creditPartnerFactoryService.getPartnerService(loanMetaDataDto.getLoanVendorName()).getKFS(loanMetaDataDto);


        }
       return kfsResponseDto;
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

    public Boolean updateOffer(UpdateSanctionOfferDto acceptSanctionOffer) throws CustomException {

        LoanApplication loanApplication = loanApplicationRepository.findById(acceptSanctionOffer.getLoanId()).orElse(null);
        if(loanApplication!=null){

            LoanApplicationStatus loanApplicationStatus = loanApplicationStatusEntityService.getLoanApplicationStatusByLoanId(loanApplication.getId());
            if(loanApplicationStatus!=null) {

                LoanMetaDataDto loanMetaDataDto = LoanMetaDataDto.builder().srCompanyId(loanApplication.getSrCompanyId()).loanVendorId(loanApplication.getLoanVendorId())
                        .loanId(loanApplication.getVendorLoanId()).internalLoanId(acceptSanctionOffer.getLoanId()).loanVendorName(acceptSanctionOffer.getLoanVendorName()).sanctionCode(loanApplicationStatus.getSanctionCode()).rejectReason(acceptSanctionOffer.getReason()).remarks(acceptSanctionOffer.getRemarks()).build();

                if(acceptSanctionOffer.getAcceptOffer()) {

                    AcceptSanctionOffer acceptSanctionOffer1 = (AcceptSanctionOffer) creditPartnerFactoryService.getPartnerService(loanMetaDataDto.getLoanVendorName()).acceptOffer(loanMetaDataDto);

                    if(acceptSanctionOffer1.getSuccess()) {
                        loanApplication.setLoanStatus(LoanStatus.LOAN_VERIFICATION);

                        loanApplicationStatus.setVendorStatus(LoanStatus.APPROVED.name());
                    }

                }else{
                     creditPartnerFactoryService.getPartnerService(loanMetaDataDto.getLoanVendorName()).rejectSanctionOffer(loanMetaDataDto);

                    loanApplication.setLoanStatus(LoanStatus.LOAN_DECLINE);

                }
                loanApplication.getAuditData().setUpdatedAt(LocalDateTime.now());
                loanApplication.getAuditData().setUpdatedBy(String.valueOf(RequestData.getUserId()));
                loanApplicationRepository.save(loanApplication);

                loanApplicationStatusEntityService.saveLoanApplicationStatus(loanApplicationStatus);
            }
        }
        return true;
    }

    private SanctionDto fetchAndSaveSanctionDetails(LoanMetaDataDto loanMetaDataDto) throws CustomException {
        SanctionDto sanctionDto =null;
        try {
            LoanApplicationStatus loanApplicationStatus = loanApplicationStatusEntityService.getLoanApplicationStatusByLoanId(loanMetaDataDto.getInternalLoanId());
            if(loanApplicationStatus==null) {
                sanctionDto = fetchAndSaveSanctionDetailsFromPartner(loanMetaDataDto);
                loanMetaDataDto.setSanctionDto(sanctionDto);
                buildLoanApplicationStatus(loanMetaDataDto);
            }else{
                SanctionDetails sanctionDetails = sanctionRepository.findBySrCompanyIdAndVendorSanctionCode(loanMetaDataDto.getSrCompanyId(), loanApplicationStatus.getSanctionCode());
                if(sanctionDetails!=null){
                   /* SanctionDto.PartnerIntegrationProject partnerIntegrationProject = MapperUtils.convertValue(sanctionDetails.getSanctionDetails(),SanctionDto.PartnerIntegrationProject.class);
                    SanctionDto.PostSanctionConditionArray postSanctionConditionArray = MapperUtils.convertValue(sanctionDetails.getSanctionDetails(),SanctionDto.PostSanctionConditionArray.class);
                    sanctionDto =SanctionDto.builder().partnerIntegrationProject(partnerIntegrationProject).postSanctionConditionsArray(postSanctionConditionArray.getPostSanctionConditionsArray()).build();
              */
                sanctionDto = MapperUtils.convertValueLowerCamelCase(sanctionDetails.getSanctionDetails(),SanctionDto.class);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(sanctionDto!=null){
            loanMetaDataDto.setVendorStatus(FlexiStatus.APPROVED.name());
            loanMetaDataDto.setSanctionDto(sanctionDto);

        }

        return sanctionDto;
    }

    private SanctionDto fetchAndSaveSanctionDetailsFromPartner(LoanMetaDataDto loanMetaDataDto) throws IOException, CustomException {

        SanctionResponseDto sanctionResponseDto = (SanctionResponseDto) creditPartnerFactoryService.getPartnerService(loanMetaDataDto.getLoanVendorName()).fetchSanctionDetails(loanMetaDataDto);

       if(sanctionResponseDto!=null && sanctionResponseDto.getSuccess()) {

           SanctionMetaDataDetails.PartnerIntegrationProject sanctionData = MapperUtils.convertValue(sanctionResponseDto.getData().getPartnerIntegrationProject(),SanctionMetaDataDetails.PartnerIntegrationProject.class);

           SanctionMetaDataDetails metaDataDetails = SanctionMetaDataDetails.builder().partnerIntegrationProject(sanctionData).postSanctionConditionsArray(sanctionResponseDto.getData().getPostSanctionConditionsArray()).build();
           loanMetaDataDto.setSanctionCode(sanctionData.getSanction_code());
           SanctionDetails sanctionDetails =  sanctionRepository.findBySrCompanyIdAndVendorSanctionCode(loanMetaDataDto.getSrCompanyId(), loanMetaDataDto.getSanctionCode());
           if(sanctionDetails==null) {

               sanctionDetails = SanctionDetails.builder().sanctionDetails(metaDataDetails)
                       .srCompanyId(loanMetaDataDto.getSrCompanyId()).vendorSanctionCode(sanctionResponseDto.getData().getPartnerIntegrationProject().getSanction_code()).loanId(loanMetaDataDto.getInternalLoanId()).build();
           }else{
               sanctionDetails.setSanctionDetails(metaDataDetails);
               sanctionDetails.setLastModifiedAt(LocalDateTime.now());
           }
           sanctionRepository.save(sanctionDetails);
           SanctionDto.PartnerIntegrationProject partnerIntegrationProject = MapperUtils.convertValue(sanctionResponseDto.getData().getPartnerIntegrationProject(),SanctionDto.PartnerIntegrationProject.class);
           return SanctionDto.builder().partnerIntegrationProject(partnerIntegrationProject).postSanctionConditionsArray(sanctionResponseDto.getData().getPostSanctionConditionsArray()).build();
       }else {
           log.info("invalid response from flexi {} ",sanctionResponseDto);
       }
        log.info(" response from flexi {} ",sanctionResponseDto);

        return null;
    }

    private LoanApplicationStatus buildLoanApplicationStatus(LoanMetaDataDto loanMetaDataDto) {

            /*BigDecimal totalDisbursementAmount = BigDecimal.ZERO;
            loanStatusUpdateWebhookDto.getDisbursementAccounts().forEach(disbursementAccount -> {
                if (disbursementAccount.getDisbursementId() != null && disbursementAccount.getDisbursedAmount()!=null) {
                    totalDisbursementAmount.add(BigDecimal.valueOf(disbursementAccount.getDisbursedAmount()));
                }
            });*/

        LoanApplicationStatus  loanApplicationStatus = loanApplicationStatusEntityService.getLoanApplicationStatusByLoanId(loanMetaDataDto.getInternalLoanId());

        if(loanApplicationStatus==null) {
            loanApplicationStatus = LoanApplicationStatus.builder().loanId(loanMetaDataDto.getInternalLoanId())
                    .vendorLoanId(loanMetaDataDto.getLoanId()).vendorStatus(loanMetaDataDto.getVendorStatus())
                    .comment("Loan Sanctioned")
                    .loanAmountApproved(BigDecimal.valueOf(Long.parseLong(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getLoanAmount().replaceAll(",", ""))))
                    .interestRate(Double.valueOf(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getAnnualRateOfInterest().replaceAll("%", "")))
                    .interestAmountAtSanction(BigDecimal.valueOf(Long.parseLong(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getTotalInterest().replaceAll(",", ""))))
                    .loanDuration(Integer.valueOf(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getLoanRepaymentPeriod())).
                    totalDisbursedAmount(BigDecimal.valueOf(Long.parseLong(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getDisbursalAmount().replaceAll(",", ""))))
                    .sanctionCode(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getSanction_code()).totalRecoverableAmount(BigDecimal.valueOf(Double.valueOf(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getTotalRepayable().replaceAll(",", ""))))
                    .totalAmountRecovered(BigDecimal.ZERO).build();
        }else{
            loanApplicationStatus.setLoanAmountApproved(BigDecimal.valueOf(Long.parseLong(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getLoanAmount().replaceAll(",", ""))));
            loanApplicationStatus.setInterestRate(Double.valueOf(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getAnnualRateOfInterest().replaceAll("%", "")));
            loanApplicationStatus.setInterestAmountAtSanction(BigDecimal.valueOf(Long.parseLong(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getTotalInterest().replaceAll(",", ""))));
            loanApplicationStatus.setLoanDuration(Integer.valueOf(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getLoanRepaymentPeriod()));
            loanApplicationStatus.setTotalDisbursedAmount(BigDecimal.valueOf(Long.parseLong(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getDisbursalAmount().replaceAll(",", ""))));
            loanApplicationStatus.setSanctionCode(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getSanction_code());
            loanApplicationStatus.setTotalRecoverableAmount(BigDecimal.valueOf(Double.valueOf(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getTotalRepayable().replaceAll(",", ""))));
            loanApplicationStatus.setSanctionCode(loanMetaDataDto.getSanctionDto().getPartnerIntegrationProject().getSanction_code());
         }

        loanApplicationStatusEntityService.saveLoanApplicationStatus(loanApplicationStatus);

        return loanApplicationStatus;
    }

}
