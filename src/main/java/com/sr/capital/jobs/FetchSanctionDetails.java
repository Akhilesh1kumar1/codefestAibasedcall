package com.sr.capital.jobs;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class FetchSanctionDetails {

    @Scheduled(fixedRateString = "${jobs.sanction.cronInMinutes}",initialDelay =70 , timeUnit = TimeUnit.MINUTES)
    @SchedulerLock(name = "fetchSanctionDetails", lockAtMostFor = "30m", lockAtLeastFor = "10m")
    public void performScheduledTask() {
        System.out.println("Executing scheduled task");
    }
}
