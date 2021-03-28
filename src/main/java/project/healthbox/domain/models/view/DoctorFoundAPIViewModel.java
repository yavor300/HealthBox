package project.healthbox.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorFoundAPIViewModel {
    private String id;
    private String firstName;
    private String lastName;
    private String locationName;
    private String imageUrl;
    private String specialtyName;
    private String education;
    private String biography;
    private String workHistory;
}
