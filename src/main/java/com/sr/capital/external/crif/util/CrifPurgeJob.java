package com.sr.capital.external.crif.util;

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
public class CrifPurgeJob {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserService userService;
    @Autowired
    private CommunicationService communicationService;

    @Autowired
    private BaseCreditPartnerEntityServiceImpl baseCreditPartnerEntityService;
    @Autowired
    private CrifPartnerService crifPartnerService;

     LoggerUtil log = LoggerUtil.getLogger(CrifPurgeJob.class);


    @Scheduled(cron = "${jobs.crif.purge.scheduledTime}")
    @SchedulerLock(name = "communicationJob", lockAtMostFor = "30m", lockAtLeastFor = "10m")
    public void performScheduledTask() {

        crifPartnerService.purgeExpiredData();
    }
}