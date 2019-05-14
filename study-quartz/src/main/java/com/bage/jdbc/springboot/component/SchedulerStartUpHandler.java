package com.bage.jdbc.springboot.component;

import com.bage.jdbc.springboot.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Chamith
 */
@Slf4j
@Component
public class SchedulerStartUpHandler implements ApplicationRunner {

    @Autowired
    private SchedulerService schedulerService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Schedule all new scheduler jobs at app startup - starting");
        try {
            schedulerService.startAllSchedulers();
            log.info("Schedule all new scheduler jobs at app startup - complete");
        } catch (Exception ex) {
            log.error("Schedule all new scheduler jobs at app startup - error", ex);
        }
    }
}
