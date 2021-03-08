package project.healthbox.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsultationDashboardViewModel {
    private String id;
    private String problemTitle;
    private String doctorFirstName;
    private String doctorLastName;
}
