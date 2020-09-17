package project.healthbox.domain.models.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsultationServiceModel extends BaseService {
    private String id;
    private Integer age;
    private String gender;
    private String diagnoses;
    private String medicaments;
    private String allergy;
    private String problemTitle;
    private String problemDescription;
    private UserServiceModel user;
    private DoctorServiceModel doctor;
}
