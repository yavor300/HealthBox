package project.healthbox.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.healthbox.domain.entities.enums.TitleEnum;
import project.healthbox.validation.annotation.FieldMatch;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@FieldMatch(
        first = "password",
        second = "confirmPassword",
        message = "Passwords must match!"
)
public class UserRegisterBindingModel {
    @Size(min = 2, max = 10, message = "The first name must be between 2 and 10 characters long!")
    private String firstName;

    @Size(min = 2, max = 10, message = "The last name must be between 2 and 10 characters long!")
    private String lastName;

    @NotNull(message = "Select your title!")
    private TitleEnum title;

    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
    message = "Enter a valid email address!")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,}$",
            message = "Password must contain minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character!")
    private String password;

    private String confirmPassword;
}