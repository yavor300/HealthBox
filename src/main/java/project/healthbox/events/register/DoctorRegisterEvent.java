package project.healthbox.events.register;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class DoctorRegisterEvent extends ApplicationEvent {
    private String firstName;
    private String lastName;
    private String email;
    private String locationName;
    private String specialtyName;

    public DoctorRegisterEvent(String firstName, String lastName, String email, String locationName, String specialtyName) {
        super(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.locationName = locationName;
        this.specialtyName = specialtyName;
    }

    @Override
    public String toString() {
        return "DoctorRegisterEvent{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", locationName='" + locationName + '\'' +
                ", specialtyName='" + specialtyName + '\'' +
                '}';
    }
}