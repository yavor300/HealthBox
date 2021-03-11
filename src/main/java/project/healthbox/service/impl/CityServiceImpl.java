package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.error.CityNotFoundException;
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
        return cityRepository.findByName(name)
                .map(city -> modelMapper.map(city, CityServiceModel.class))
                .orElseThrow(() -> new CityNotFoundException("Invalid city name!"));
    }

    @Override
    public List<CityServiceModel> getAll() {
        return cityRepository.findAll().stream()
                .map(city -> modelMapper.map(city, CityServiceModel.class))
                .collect(Collectors.toList());
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
        //TODO USE OPTIONAL, THROW ERROR IF IS PRESENT

        if (cityRepository.findByName(name).isPresent()) {
            return  null;
        }
        return modelMapper.map(cityRepository.saveAndFlush(new City(name)), CityServiceModel.class);
    }
}