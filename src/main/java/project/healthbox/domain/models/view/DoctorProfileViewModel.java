package project.healthbox.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.healthbox.domain.models.service.CityServiceModel;

@Getter
@Setter
@NoArgsConstructor
public class DoctorProfileViewModel {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String locationName;
    private String education;
    private String biography;
    private String workHistory;
    private String imageUrl;
}
