package project.healthbox.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class AnswerSendBindingModel {
    @NotBlank(message = "Answer cannot be empty!")
    private String problemAnswer;
}
