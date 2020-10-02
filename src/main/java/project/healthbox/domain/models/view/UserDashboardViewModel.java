package project.healthbox.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDashboardViewModel {
    private String id;
    private String problemTitle;
    private String doctorFirstName;
    private String doctorLastName;
}
