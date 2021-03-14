package project.healthbox.events.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;

@Component
@Getter
@Setter
@AllArgsConstructor
public class RegisterEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishUserRegisterEvent(UserServiceModel userServiceModel) {
        UserRegisterEvent userRegisterEvent = new UserRegisterEvent(
                userServiceModel.getFirstName(),
                userServiceModel.getLastName(),
                userServiceModel.getEmail());

        applicationEventPublisher.publishEvent(userRegisterEvent);
    }

    public void publishDoctorRegisterEvent(DoctorServiceModel doctorServiceModel) {
        DoctorRegisterEvent doctorRegisterEvent = new DoctorRegisterEvent(
                doctorServiceModel.getFirstName(),
                doctorServiceModel.getLastName(),
                doctorServiceModel.getEmail(),
                doctorServiceModel.getLocation().getName(),
                doctorServiceModel.getSpecialty().getName());

        applicationEventPublisher.publishEvent(doctorRegisterEvent);
    }
}