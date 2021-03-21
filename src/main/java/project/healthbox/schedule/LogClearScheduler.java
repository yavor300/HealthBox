package project.healthbox.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class LogClearScheduler {

    @Scheduled(cron = "0 0 0 1 */6 *")
    public void clearRegistrationLogEverySixMonths() {
        new File("src/main/java/project/healthbox/events/log/files/Registration.log").delete();
    }

    @Scheduled(cron = "0 0 0 1 */6 *")
    public void clearConsultationLogEverySixMonths() {
        new File("src/main/java/project/healthbox/events/log/files/Consultation.log").delete();
    }
}