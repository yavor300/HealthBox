package project.healthbox.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.healthbox.domain.models.service.AnswerServiceModel;

@Getter
@Setter
@NoArgsConstructor
public class ConsultationDetailsViewModel {
    private String id;
    private Integer age;
    private String gender;
    private String diagnoses;
    private String medicaments;
    private String allergy;
    private String problemTitle;
    private String problemDescription;
    private String problemAnswer;
}
