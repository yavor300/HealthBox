package project.healthbox.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

import java.io.IOException;

@Component
public class LogClearScheduler {

    @Scheduled(cron = "0 0 0 1 */6 *")
    public void clearLogEverySixMonths() throws IOException {
        new File("src/main/java/project/healthbox/events/log/files/ApplicationLog.log").delete();
    }
}