package project.healthbox.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.repostory.CityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, ModelMapper modelMapper) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CityServiceModel getByName(String name) {
        City city = this.cityRepository.getByName(name);
        if (city != null) {
            return this.modelMapper.map(city, CityServiceModel.class);
        } else {
            return null;
        }
    }

    @Override
    public List<CityServiceModel> getAll() {
        return this.cityRepository.findAll().stream()
                .map(c -> this.modelMapper.map(c, CityServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public String getIdByCityName(String name) {
        CityServiceModel cityServiceModel = this.getByName(name);
        if (cityServiceModel == null) {
            return "";
        }
        return cityServiceModel.getId();
    }
}
