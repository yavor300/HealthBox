//package project.healthbox.validation.user;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.Errors;
//import project.healthbox.domain.models.binding.UserRegisterBindingModel;
//import project.healthbox.repostory.UserRepository;
//import project.healthbox.validation.ValidationConstants;
//import project.healthbox.validation.annotation.Validator;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Validator
//public class UserRegisterValidator  implements org.springframework.validation.Validator {
//    private final UserRepository userRepository;
//
//    @Autowired
//    public UserRegisterValidator(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return UserRegisterBindingModel.class.equals(aClass);
//    }
//
//    @Override
//    public void validate(Object o, Errors errors) {
//        UserRegisterBindingModel userRegisterBindingModel = (UserRegisterBindingModel) o;
//
//        if (userRegisterBindingModel.getFirstName().length() < 3 || userRegisterBindingModel.getFirstName().length() > 10) {
//            errors.rejectValue(
//                    "firstName",
//                    ValidationConstants.FIRST_NAME_LENGTH,
//                    ValidationConstants.FIRST_NAME_LENGTH
//            );
//        }
//
//        if (userRegisterBindingModel.getLastName().length() < 3 || userRegisterBindingModel.getLastName().length() > 10) {
//            errors.rejectValue(
//                    "lastName",
//                    ValidationConstants.SECOND_NAME_LENGTH,
//                    ValidationConstants.SECOND_NAME_LENGTH
//            );
//        }
//
//        if (userRegisterBindingModel.getTitle() == null || userRegisterBindingModel.getTitle().trim().isEmpty()) {
//            errors.rejectValue(
//                    "title",
//                    ValidationConstants.TITLE_NOT_SELECTED,
//                    ValidationConstants.TITLE_NOT_SELECTED
//            );
//        }
//
//        if (this.userRepository.findByEmail(userRegisterBindingModel.getEmail()).isPresent()) {
//            errors.rejectValue(
//                    "email",
//                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, userRegisterBindingModel.getEmail()),
//                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, userRegisterBindingModel.getEmail())
//            );
//        }
//
//        if (!this.isPasswordValidByRegex(userRegisterBindingModel.getPassword())) {
//            errors.rejectValue(
//                    "password",
//                    ValidationConstants.PASSWORD_DO_NOT_MATCH_REGEX,
//                    ValidationConstants.PASSWORD_DO_NOT_MATCH_REGEX
//            );
//        }
//
//        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
//            errors.rejectValue(
//                    "confirmPassword",
//                    ValidationConstants.PASSWORDS_DO_NOT_MATCH,
//                    ValidationConstants.PASSWORDS_DO_NOT_MATCH
//            );
//        }
//
//    }
//
//    private boolean isPasswordValidByRegex(String password) {
//        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(password);
//        return matcher.matches();
//    }
//}
