package project.healthbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HealthboxApplication {
    public static void main(String[] args) {
        SpringApplication.run(HealthboxApplication.class, args);
    }

}
