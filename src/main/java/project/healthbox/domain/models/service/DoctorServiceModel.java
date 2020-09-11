package project.healthbox.domain.models.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorServiceModel {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String location;
    private String education;
    private String biography;
    private String workHistory;
    private SpecialtyServiceModel specialty;
}
