package project.healthbox.service;

import project.healthbox.domain.models.service.SpecialtyServiceModel;

import java.util.List;

public interface SpecialtyService {
    List<SpecialtyServiceModel> getAll();

    SpecialtyServiceModel findByName(String name);

    String getIdBySpecialtyName(String name);

    SpecialtyServiceModel getById(String id);

    void deleteSpecialty(String id);

    SpecialtyServiceModel createSpecialty(String name);
}
