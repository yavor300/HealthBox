package project.healthbox.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class DoctorUpdateBindingModel {
    private String id;
    private String location;
    private String education;
    private String biography;
    private String workHistory;
    private String specialty;
    private MultipartFile image;
}
