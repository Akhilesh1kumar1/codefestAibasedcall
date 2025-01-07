package com.sr.capital.jobs;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

//@Component
public class LoanStatusUpdateJob {

   /* @Scheduled(fixedRateString = "${jobs.status.cronInMinutes}",initialDelay =50 , timeUnit = TimeUnit.MINUTES)
    @SchedulerLock(name = "LoanStatusUpdateJob", lockAtMostFor = "30m", lockAtLeastFor = "10m")
    public void performScheduledTask() {
        System.out.println("Executing scheduled task");
    }*/
}
