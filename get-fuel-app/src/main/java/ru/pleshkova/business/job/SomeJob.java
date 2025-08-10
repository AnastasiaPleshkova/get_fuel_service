package ru.pleshkova.business.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty("job.some-job.enabled")
public class SomeJob {

//    private final CarRepository carRepository;

    private static final String JOB_NAME = "'someJob'";

    @Scheduled(cron = "#{@jobProperties.someJob.cron")
    @SchedulerLock(name = "someJob")
    public void someJob() {
        log.info("{}: start job at {}", JOB_NAME, LocalDateTime.now());

        // job

        log.info("{}: end job at {}", JOB_NAME, LocalDateTime.now());
    }
}
