package project.healthbox.domain.models.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class DoctorServiceModel extends BaseService {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private CityServiceModel location;
    private String education;
    private String biography;
    private String workHistory;
    private MultipartFile image;
    private String imageUrl;
    private SpecialtyServiceModel specialty;
    private List<ConsultationServiceModel> consultations;
    private List<UserServiceModel> users;
    private Set<RoleServiceModel> authorities;
}
