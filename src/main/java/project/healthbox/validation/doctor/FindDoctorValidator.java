//package project.healthbox.validation.doctor;
//
//import org.springframework.validation.Errors;
//import project.healthbox.domain.models.binding.ChooseSpecialistBindingModel;
//import project.healthbox.validation.ValidationConstants;
//import project.healthbox.validation.annotation.Validator;
//
//@Validator
//public class FindDoctorValidator  implements org.springframework.validation.Validator {
//
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return ChooseSpecialistBindingModel.class.equals(aClass);
//    }
//
//    @Override
//    public void validate(Object o, Errors errors) {
//        ChooseSpecialistBindingModel chooseSpecialistBindingModel = (ChooseSpecialistBindingModel) o;
//
//        if ((chooseSpecialistBindingModel.getSpecialty() == null || chooseSpecialistBindingModel.getSpecialty().trim().isEmpty())
//        && (chooseSpecialistBindingModel.getLocation() == null || chooseSpecialistBindingModel.getLocation().trim().isEmpty())
//        && (chooseSpecialistBindingModel.getDoctorName() == null || chooseSpecialistBindingModel.getDoctorName().trim().isEmpty())) {
//            errors.rejectValue(
//                    "doctorName",
//                    ValidationConstants.NO_CHOSEN_CRITERIA,
//                    ValidationConstants.NO_CHOSEN_CRITERIA
//            );
//        }
//
//
//    }
//}
