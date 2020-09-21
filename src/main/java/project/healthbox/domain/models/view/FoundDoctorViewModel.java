package project.healthbox.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoundDoctorViewModel {
    private String id;
    private String firstName;
    private String lastName;
    private String locationName;
}
