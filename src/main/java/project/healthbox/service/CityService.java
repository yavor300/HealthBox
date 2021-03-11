package project.healthbox.service;

import project.healthbox.domain.models.service.CityServiceModel;

import java.util.List;

public interface CityService {
    CityServiceModel getByName(String name);

    List<CityServiceModel> getAll();

    CityServiceModel getById(String id);

    void deleteCity(String id);

    CityServiceModel createCity(String name);
}
