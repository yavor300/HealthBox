package project.healthbox.events.register;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UserRegisterEvent extends ApplicationEvent {
    private String firstName;
    private String lastName;
    private String email;


    public UserRegisterEvent(String firstName, String lastName, String email) {
        super(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserRegisterEvent{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}