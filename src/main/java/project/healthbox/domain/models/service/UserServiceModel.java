package project.healthbox.domain.models.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserServiceModel extends BaseServiceModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<ConsultationServiceModel> consultations;
    private List<DoctorServiceModel> doctors;
    private Set<RoleServiceModel> authorities;
}
