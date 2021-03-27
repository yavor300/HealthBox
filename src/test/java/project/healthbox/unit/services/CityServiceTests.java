package project.healthbox.unit.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.error.ObjectAlreadyExistsException;
import project.healthbox.error.ObjectNotFoundException;
import project.healthbox.repostory.CityRepository;
import project.healthbox.service.CityService;
import project.healthbox.service.impl.CityServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class CityServiceTests {
    private final static String CITY_NAME = "CITY_NAME";
    private final static String CITY_ID = "UUID";

    private CityRepository mockCityRepository;
    private CityService cityService;

    private City city;

    @Before
    public void init() {
        mockCityRepository = Mockito.mock(CityRepository.class);
        cityService = new CityServiceImpl(mockCityRepository, new ModelMapper());

        city = new City();
        city.setId(CITY_ID);
        city.setName(CITY_NAME);
    }

    @Test
    public void getByName_Should_Return_CorrectCity() {
        Mockito.when(mockCityRepository.findByName(CITY_NAME))
                .thenReturn(Optional.of(city));

        CityServiceModel cityServiceModel = cityService.getByName(CITY_NAME);

        assertEquals(city.getId(), cityServiceModel.getId());
        assertEquals(city.getName(), cityServiceModel.getName());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getByName_Should_ThrowAnException_If_CityDoesNotExist() {
        Mockito.when(mockCityRepository.findByName(CITY_NAME))
                .thenReturn(Optional.empty());

        cityService.getByName(CITY_NAME);
    }

    @Test
    public void getAll_Should_Return_CollectionOfCities() {
        Mockito.when(mockCityRepository.findAll())
                .thenReturn(List.of(city));

        List<CityServiceModel> result = cityService.getAll();
        CityServiceModel cityServiceModel = result.get(0);

        assertEquals(1, result.size());
        assertEquals(city.getName(), cityServiceModel.getName());
        assertEquals(city.getId(), cityServiceModel.getId());
    }

    @Test
    public void getById_Should_Return_CorrectCity() {
        Mockito.when(mockCityRepository.findById(CITY_ID))
                .thenReturn(Optional.of(city));

        CityServiceModel cityServiceModel = cityService.getById(CITY_ID);

        assertEquals(city.getId(), cityServiceModel.getId());
        assertEquals(city.getName(), cityServiceModel.getName());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getById_Should_ThrowAnException_If_CityDoesNotExist() {
        Mockito.when(mockCityRepository.findById(CITY_ID))
                .thenReturn(Optional.empty());

        cityService.getById(CITY_ID);
    }

    @Test
    public void deleteCity_Should_ExecuteCorrectly() {
        Mockito.when(mockCityRepository.findById(CITY_ID))
                .thenReturn(Optional.of(city));

        cityService.deleteCity(CITY_ID);

        Mockito.verify(mockCityRepository, times(1)).delete(city);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteCity_Should_ThrownAnException() {
        Mockito.when(mockCityRepository.findById(CITY_ID))
                .thenReturn(Optional.empty());

        cityService.deleteCity(CITY_ID);
    }

    @Test
    public void createCity_Should_Save_And_Return_TheCity() {
        Mockito.when(mockCityRepository.findByName(CITY_NAME))
                .thenReturn(Optional.empty());

        Mockito.when(mockCityRepository.saveAndFlush(any(City.class)))
                .thenReturn(city);

        CityServiceModel result = cityService.createCity(CITY_NAME);

        Assert.assertEquals(city.getId(), result.getId());
    }

    @Test(expected = ObjectAlreadyExistsException.class)
    public void createCity_Should_ThrownAnException() {
        Mockito.when(mockCityRepository.findByName(CITY_NAME))
                .thenReturn(Optional.of(city));

        cityService.createCity(CITY_NAME);
    }
}