package project.healthbox.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.error.ObjectAlreadyExistsException;
import project.healthbox.error.ObjectNotFoundException;
import project.healthbox.repostory.CityRepository;
import project.healthbox.service.CityService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;
    private final Resource citiesFile;
    private final Gson gson;

    public CityServiceImpl(CityRepository cityRepository, ModelMapper modelMapper, @Value("classpath:init/cities.json") Resource citiesFile, Gson gson) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
        this.citiesFile = citiesFile;
        this.gson = gson;
    }

    @Override
    public void seedCities() {
        if (cityRepository.count() == 0) {
            try {
                City[] cities =
                        gson.fromJson(Files.readString(Path.of(citiesFile.getURI())), City[].class);

                Arrays.stream(cities).
                        forEach(cityRepository::save);
            } catch (IOException e) {
                throw new IllegalStateException("Cannot seed cities...");
            }
        }
    }

    @Override
    public CityServiceModel getByName(String name) {
        return cityRepository.findByName(name)
                .map(city -> modelMapper.map(city, CityServiceModel.class))
                .orElseThrow(() -> new ObjectNotFoundException("Invalid city name!"));
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
                .orElseThrow(() -> new ObjectNotFoundException("Invalid city identifier!"));
    }

    @Override
    public void deleteCity(String id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid city identifier!"));
        cityRepository.delete(city);
    }

    @Override
    public CityServiceModel createCity(String name) {
        if (cityRepository.findByName(name).isPresent()) {
            throw new ObjectAlreadyExistsException("City with that name is already present in the database!");
        }
        return modelMapper.map(cityRepository.saveAndFlush(new City(name)), CityServiceModel.class);
    }
}