package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.error.CityAlreadyExistsException;
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
        return cityRepository.findById(id)
                .map(city -> modelMapper.map(city, CityServiceModel.class))
                .orElseThrow(() -> new CityNotFoundException("Invalid city!"));
    }

    @Override
    public CityServiceModel deleteCity(String id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFoundException("Invalid city!"));
        cityRepository.delete(city);
        return modelMapper.map(city, CityServiceModel.class);
    }

    @Override
    public CityServiceModel createCity(String name) {
        if (cityRepository.findByName(name).isPresent()) {
            throw new CityAlreadyExistsException("City with that name is already present in the database!");
        }
        return modelMapper.map(cityRepository.saveAndFlush(new City(name)), CityServiceModel.class);
    }
}