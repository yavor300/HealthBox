package project.healthbox.domain.models.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.healthbox.domain.entities.enums.GenderEnum;

@Getter
@Setter
@NoArgsConstructor
public class ConsultationServiceModel extends BaseServiceModel {
    private Integer age;
    private GenderEnum gender;
    private String diagnoses;
    private String medicaments;
    private String allergy;
    private String problemTitle;
    private String problemDescription;
    private UserServiceModel user;
    private DoctorServiceModel doctor;
    private AnswerServiceModel answer;
}
