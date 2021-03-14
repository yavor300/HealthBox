package project.healthbox.events.register;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import project.healthbox.service.LoggerService;

import java.io.IOException;

@Component
@AllArgsConstructor
public class RegisterEventListener {
    private final LoggerService loggerService;

    @EventListener(UserRegisterEvent.class)
    public void onUserRegisterEvent(UserRegisterEvent userRegisterEvent) throws IOException {
        loggerService.log(userRegisterEvent);
    }

    @EventListener(DoctorRegisterEvent.class)
    public void onDoctorRegisterEvent(DoctorRegisterEvent doctorRegisterEvent) throws IOException {
        loggerService.log(doctorRegisterEvent);
    }
}