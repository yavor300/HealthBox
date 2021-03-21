package project.healthbox.aop;

import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import project.healthbox.domain.models.service.ConsultationServiceModel;

import java.io.FileWriter;
import java.io.IOException;

@Aspect
@Component
@AllArgsConstructor
public class ConsultationAspect {

    @Pointcut("execution(* project.healthbox.service.impl.ConsultationServiceImpl.save(..))")
    public void trackSave() {}

    @AfterReturning(pointcut = "trackSave()", returning = "consultationServiceModel")
    public void loggingAfterReturning(JoinPoint joinPoint, Object consultationServiceModel) throws IOException {
        ConsultationServiceModel consultationDetails = (ConsultationServiceModel) consultationServiceModel;
        FileWriter myWriter = new FileWriter("src/main/java/project/healthbox/events/log/files/Consultation.log", true);
        myWriter.write(String.format("User with email - %s - sent consultation to a doctor with email - %s%n",
                consultationDetails.getUser().getEmail(),
                consultationDetails.getDoctor().getEmail()));
        myWriter.close();
    }
}