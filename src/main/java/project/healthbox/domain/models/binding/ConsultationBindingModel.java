package project.healthbox.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsultationBindingModel {
    private String id;
    private Integer age;
    private String gender;
    private String diagnoses;
    private String medicaments;
    private String allergy;
    private String problemDescription;
}
