package project.healthbox.service;

import project.healthbox.domain.models.service.SpecialtyServiceModel;

import java.util.List;

public interface SpecialtyService {
    List<SpecialtyServiceModel> getAll();

    SpecialtyServiceModel getByName(String name);

    SpecialtyServiceModel getById(String id);

    void deleteSpecialty(String id);

    SpecialtyServiceModel createSpecialty(String name);

    void seedSpecialties();
}
