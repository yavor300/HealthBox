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


//        if (doctorUpdateBindingModel.getSpecialty() == null || doctorUpdateBindingModel.getSpecialty().trim().isEmpty()) {
//            errors.rejectValue(
//                    "specialty",
//                    ValidationConstants.SPECIALTY_NOT_SELECTED,
//                    ValidationConstants.SPECIALTY_NOT_SELECTED
//            );
//        }
//
//        if (doctorUpdateBindingModel.getLocation() == null || doctorUpdateBindingModel.getLocation().trim().isEmpty()) {
//            errors.rejectValue(
//                    "location",
//                    ValidationConstants.LOCATION_NOT_SELECTED,
//                    ValidationConstants.LOCATION_NOT_SELECTED
//            );
//        }
//
//        if (doctorUpdateBindingModel.getBiography() == null || doctorUpdateBindingModel.getBiography().trim().isEmpty()) {
//            errors.rejectValue(
//                    "biography",
//                    ValidationConstants.EMPTY_TEXTAREA,
//                    ValidationConstants.EMPTY_TEXTAREA
//            );
//        }
//
//        if (doctorUpdateBindingModel.getEducation() == null || doctorUpdateBindingModel.getEducation().trim().isEmpty()) {
//            errors.rejectValue(
//                    "education",
//                    ValidationConstants.EMPTY_TEXTAREA,
//                    ValidationConstants.EMPTY_TEXTAREA
//            );
//        }
//
//        if (doctorUpdateBindingModel.getWorkHistory() == null || doctorUpdateBindingModel.getWorkHistory().trim().isEmpty()) {
//            errors.rejectValue(
//                    "workHistory",
//                    ValidationConstants.EMPTY_TEXTAREA,
//                    ValidationConstants.EMPTY_TEXTAREA
//            );
//        }
    }
}
