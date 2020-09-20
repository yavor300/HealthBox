package project.healthbox.validation.consultation;

import org.springframework.validation.Errors;
import project.healthbox.domain.models.binding.ConsultationBindingModel;
import project.healthbox.validation.ValidationConstants;
import project.healthbox.validation.annotation.Validator;

@Validator
public class ConsultationFormValidator  implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ConsultationBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ConsultationBindingModel consultationBindingModel = (ConsultationBindingModel) o;

        if (consultationBindingModel.getAge() == null || consultationBindingModel.getAge() <= 0) {
            errors.rejectValue(
                    "age",
                    ValidationConstants.INVALID_AGE,
                    ValidationConstants.INVALID_AGE
            );
        }

        if (consultationBindingModel.getGender() == null || consultationBindingModel.getGender().trim().isEmpty()) {
            errors.rejectValue(
                    "gender",
                    ValidationConstants.GENDER_NOT_SELECTED,
                    ValidationConstants.GENDER_NOT_SELECTED
            );
        }

        if (consultationBindingModel.getProblemTitle() == null || consultationBindingModel.getProblemTitle().trim().isEmpty()) {
            errors.rejectValue(
                    "problemTitle",
                    ValidationConstants.EMPTY_PROBLEM_TITLE,
                    ValidationConstants.EMPTY_PROBLEM_TITLE
            );
        }

        if (consultationBindingModel.getProblemDescription() == null || consultationBindingModel.getProblemDescription().trim().isEmpty()) {
            errors.rejectValue(
                    "problemDescription",
                    ValidationConstants.EMPTY_TEXTAREA,
                    ValidationConstants.EMPTY_TEXTAREA
            );
        }
    }
}
