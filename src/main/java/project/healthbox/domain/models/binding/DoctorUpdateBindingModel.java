package project.healthbox.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class DoctorUpdateBindingModel {
    private MultipartFile image;

    @NotBlank(message = "Specialty cannot be empty!")
    private String specialtyName;

    @NotBlank(message = "Location cannot be empty!")
    private String locationName;

    @Size(min = 10, message = "Biography must be at least 10 characters!")
    private String biography;

    @Size(min = 10, message = "Eduction must be at least 10 characters!")
    private String education;

    @Size(min = 10, message = "Work history must be at least 10 characters!")
    private String workHistory;
}
