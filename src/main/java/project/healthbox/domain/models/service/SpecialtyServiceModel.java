package project.healthbox.domain.models.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SpecialtyServiceModel extends BaseServiceModel {
    private String name;
    private List<DoctorServiceModel> doctors;
}
