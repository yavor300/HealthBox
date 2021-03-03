package project.healthbox.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ConsultationSendBindingModel {
    //TODO REMOVE THE ID -> UNNECESSARY INFORMATION
    private String id;

    @Positive(message = "Age must be a positive number!")
    @NotNull(message = "Age cannot be empty!")
    private Integer age;

    @NotBlank(message = "Gender cannot be empty!")
    private String gender;

    @NotBlank(message = "Diagnoses area cannot be empty!")
    private String diagnoses;

    @NotBlank(message = "Medicaments area cannot be empty!")
    private String medicaments;

    @NotBlank(message = "Allergies area cannot be empty!")
    private String allergy;

    @NotBlank(message = "Problem title cannot be empty!")
    private String problemTitle;

    @Size(min = 10, message = "Problem description must be at least 10 characters.")
    private String problemDescription;
}
