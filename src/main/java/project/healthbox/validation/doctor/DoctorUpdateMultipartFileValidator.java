package project.healthbox.validation.doctor;

import org.springframework.validation.Errors;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.validation.ValidationConstants;
import project.healthbox.validation.annotation.Validator;

@Validator
public class DoctorUpdateMultipartFileValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return DoctorUpdateBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DoctorUpdateBindingModel doctorUpdateBindingModel = (DoctorUpdateBindingModel) o;

        if (doctorUpdateBindingModel.getImage().isEmpty()) {
            errors.rejectValue(
                    "image",
                    ValidationConstants.PROFILE_PICTURE_NOT_UPLOADED,
                    ValidationConstants.PROFILE_PICTURE_NOT_UPLOADED
            );
        }
    }
}