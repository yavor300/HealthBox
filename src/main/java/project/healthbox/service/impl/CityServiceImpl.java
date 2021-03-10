package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.repostory.CityRepository;
import project.healthbox.service.CityService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;

    @Override
    public CityServiceModel getByName(String name) {
        City city = cityRepository.getByName(name);
        if (city != null) {
            return modelMapper.map(city, CityServiceModel.class);
        } else {
            return null;
        }
    }

    @Override
    public List<CityServiceModel> getAll() {
        return cityRepository.findAll().stream()
                .map(city -> modelMapper.map(city, CityServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public String getIdByCityName(String name) {
        CityServiceModel cityServiceModel = getByName(name);
        if (cityServiceModel == null) {
            return "";
        }
        return cityServiceModel.getId();
    }

    @Override
    public CityServiceModel getById(String id) {
        return modelMapper.map(cityRepository.getById(id), CityServiceModel.class);
    }

    @Override
    public void deleteCity(String id) {
        City city = cityRepository.getById(id);
        cityRepository.delete(city);
    }

    @Override
    public CityServiceModel createCity(String name) {
        if (cityRepository.findByName(name).isPresent()) {
            return null;
        }
        return modelMapper.map(cityRepository.saveAndFlush(new City(name)), CityServiceModel.class);
    }
}