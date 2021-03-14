package project.healthbox.events.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import project.healthbox.domain.models.service.UserServiceModel;

@Component
@Getter
@Setter
@AllArgsConstructor
public class UserRegisterEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(UserServiceModel userServiceModel) {
        UserRegisterEvent userRegisterEvent = new UserRegisterEvent(
                userServiceModel.getFirstName(),
                userServiceModel.getLastName(),
                userServiceModel.getEmail());

        applicationEventPublisher.publishEvent(userRegisterEvent);
    }
}
