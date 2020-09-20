package project.healthbox.service;

import project.healthbox.domain.models.service.CityServiceModel;

import java.util.List;

public interface CityService {
    CityServiceModel getByName(String name);
    List<CityServiceModel> getAll();
}
