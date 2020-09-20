package project.healthbox.validation.answer;

import org.springframework.validation.Errors;
import project.healthbox.domain.models.binding.SendAnswerBindingModel;
import project.healthbox.validation.ValidationConstants;
import project.healthbox.validation.annotation.Validator;

@Validator
public class AnswerValidator  implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return SendAnswerBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SendAnswerBindingModel sendAnswerBindingModel = (SendAnswerBindingModel) o;

        if (sendAnswerBindingModel.getProblemAnswer() == null || sendAnswerBindingModel.getProblemAnswer().trim().isEmpty()) {
            errors.rejectValue(
                    "problemAnswer",
                    ValidationConstants.EMPTY_TEXTAREA,
                    ValidationConstants.EMPTY_TEXTAREA
            );
        }
    }
}
