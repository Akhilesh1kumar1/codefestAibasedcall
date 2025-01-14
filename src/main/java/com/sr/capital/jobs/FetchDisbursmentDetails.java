package com.sr.capital.jobs;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

//@Component
public class FetchDisbursmentDetails {


    /*@Scheduled(fixedRateString = "${jobs.disbursement.cronInMinutes}",initialDelay =65 , timeUnit = TimeUnit.MINUTES)
    @SchedulerLock(name = "fetchDisbursementDetails", lockAtMostFor = "30m", lockAtLeastFor = "10m")
    public void performScheduledTask() {
        System.out.println("Executing scheduled task");
    }*/
}
