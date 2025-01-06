package com.sr.capital.jobs;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.UserDetails;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.User;
import com.sr.capital.external.crif.service.CrifPartnerService;
import com.sr.capital.external.dto.request.CommunicationRequestTemp;
import com.sr.capital.external.service.CommunicationService;
import com.sr.capital.helpers.enums.CommunicationTemplateNames;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.helpers.enums.LoanVendorName;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.UserService;
import com.sr.capital.service.entityimpl.BaseCreditPartnerEntityServiceImpl;
import com.sr.capital.util.LoggerUtil;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
public class CommunicationJob {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserService userService;
    @Autowired
    private CommunicationService communicationService;

     LoggerUtil log = LoggerUtil.getLogger(CommunicationJob.class);


    @Scheduled(cron = "${jobs.communication.scheduledTime}")
    @SchedulerLock(name = "communicationJob", lockAtMostFor = "30m", lockAtLeastFor = "10m")
    public void performScheduledTask() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(2);
        LocalDateTime endDate = now.minusDays(1);

        int page = 0;
        int size = 100; // Batch size for processing

        Pageable pageable = PageRequest.of(page, size);
        Page<LoanApplication> loanApplications;
        List<LoanStatus> loanStatusList = new ArrayList<>();
        loanStatusList.addAll(List.of(LoanStatus.LEAD_VERIFIED,LoanStatus.LEAD_INITIATED,LoanStatus.LEAD_IN_PROGRESS));

        List<String> loanStatusListInString = loanStatusList.stream().map(Enum::name).toList();
        List<LoanApplication> loanApplicationList = new ArrayList<>();

        do {
            loanApplications = loanApplicationRepository.findAllByDateRangeAndStatusList(startDate, endDate,loanStatusListInString, pageable);
            loanApplicationList.addAll(loanApplications.getContent());

            page++;
            pageable = PageRequest.of(page, size);
        } while (!loanApplications.isLast());

        if(CollectionUtils.isNotEmpty(loanApplicationList)){
            processLoanApplicationBatch(loanApplicationList);
        }
    }

    private void processLoanApplicationBatch(List<LoanApplication> loanApplicationList) {

        try {

            String subject = "Complete your Loan Application in Just a Few Clicks!";
            String templateName = CommunicationTemplateNames.REMINDER_EMAIL.getTemplateName();
            loanApplicationList.forEach(loanApplication -> {
                CommunicationRequestTemp.MetaData metaData = CommunicationRequestTemp.MetaData.builder().loanId(loanApplication.getId().toString())
                        .requestedLoanAmount(loanApplication.getLoanAmountRequested()).vendorName(LoanVendorName.FLEXI.getLoanVendorName())
                        .capitalUrl(appProperties.getCapitalWebUrl()).comments(loanApplication.getComments()).requestedLoanTenure(loanApplication.getLoanDuration()).state(loanApplication.getState()).build();


                UserDetails user =  userService.getCompanyDetailsWithoutEncryption(loanApplication.getSrCompanyId());

                if(user!=null) {

                    CommunicationRequestTemp.EmailCommunicationDTO emailCommunicationDTO = CommunicationRequestTemp.EmailCommunicationDTO.builder()
                            .recipientEmail(user.getEmail()).recipientName(user.getFirstName()).subject(subject).build();

                    CommunicationRequestTemp communicationRequestTemp = CommunicationRequestTemp.builder().contentMetaData(metaData).emailCommunicationDto(emailCommunicationDTO).templateName(templateName).build();

                    communicationService.sendCommunicationForLoan(communicationRequestTemp);
                }
            });
        }catch (Exception ex){
            log.error("error in communication  "+ex.getMessage());
        }
    }
}
