//package project.healthbox.validation.consultation;
//
//import org.springframework.validation.Errors;
//import project.healthbox.domain.models.binding.ConsultationSendBindingModel;
//import project.healthbox.validation.ValidationConstants;
//import project.healthbox.validation.annotation.Validator;
//
//@Validator
//public class ConsultationFormValidator  implements org.springframework.validation.Validator {
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return ConsultationSendBindingModel.class.equals(aClass);
//    }
//
//    @Override
//    public void validate(Object o, Errors errors) {
//        ConsultationSendBindingModel consultationSendBindingModel = (ConsultationSendBindingModel) o;
//
//        if (consultationSendBindingModel.getAge() == null || consultationSendBindingModel.getAge() <= 0) {
//            errors.rejectValue(
//                    "age",
//                    ValidationConstants.INVALID_AGE,
//                    ValidationConstants.INVALID_AGE
//            );
//        }
//
//        if (consultationSendBindingModel.getGender() == null || consultationSendBindingModel.getGender().trim().isEmpty()) {
//            errors.rejectValue(
//                    "gender",
//                    ValidationConstants.GENDER_NOT_SELECTED,
//                    ValidationConstants.GENDER_NOT_SELECTED
//            );
//        }
//
//        if (consultationSendBindingModel.getProblemTitle() == null || consultationSendBindingModel.getProblemTitle().trim().isEmpty()) {
//            errors.rejectValue(
//                    "problemTitle",
//                    ValidationConstants.EMPTY_PROBLEM_TITLE,
//                    ValidationConstants.EMPTY_PROBLEM_TITLE
//            );
//        }
//
//        if (consultationSendBindingModel.getProblemDescription() == null || consultationSendBindingModel.getProblemDescription().trim().isEmpty()) {
//            errors.rejectValue(
//                    "problemDescription",
//                    ValidationConstants.EMPTY_TEXTAREA,
//                    ValidationConstants.EMPTY_TEXTAREA
//            );
//        }
//    }
//}
