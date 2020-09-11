package project.healthbox.domain.models.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SpecialtyServiceModel extends BaseService {
    private String name;
    private List<DoctorServiceModel> doctors;
}
