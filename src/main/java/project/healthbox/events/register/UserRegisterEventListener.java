package project.healthbox.events.register;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import project.healthbox.service.LoggerService;

import java.io.IOException;

@Component
@AllArgsConstructor
public class UserRegisterEventListener {
    private final LoggerService<UserRegisterEvent> loggerService;

    @EventListener(UserRegisterEvent.class)
    public void onUserRegisterEvent(UserRegisterEvent userRegisterEvent) throws IOException {
        loggerService.log(userRegisterEvent);
    }
}