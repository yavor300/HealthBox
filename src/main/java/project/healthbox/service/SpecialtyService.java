package project.healthbox.service;

import project.healthbox.domain.models.service.SpecialtyServiceModel;

import java.util.List;

public interface SpecialtyService {
    List<SpecialtyServiceModel> getAll();
    SpecialtyServiceModel findByName(String name);
}
