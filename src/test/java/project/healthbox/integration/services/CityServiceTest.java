package project.healthbox.integration.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.repostory.CityRepository;
import project.healthbox.service.CityService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CityServiceTest {
    @Autowired
    private CityService service;

    @MockBean
    private CityRepository mockCityRepository;

    @Test
    public void getByNameMethodShouldReturnCorrectCity() {
        String cityName = "City Name";

        City city = new City();
        city.setName(cityName);

        Mockito.when(mockCityRepository.getByName(cityName))
                .thenReturn(city);

        CityServiceModel cityServiceModel = service.getByName(cityName);

        assertEquals(city.getName(), cityServiceModel.getName());
    }

    @Test
    public void getByNameMethodShouldReturnNullWhenCityDoesNotExist() {
        String cityName = "City Name";

        Mockito.when(mockCityRepository.getByName(cityName))
                .thenReturn(null);

        CityServiceModel cityServiceModel = service.getByName(cityName);

        assertNull(cityServiceModel);
    }

    @Test
    public void getByAllMethodShouldReturnCollectionOfCities() {
        List<City> cities = new ArrayList<>();
        cities.add(new City());

        Mockito.when(mockCityRepository.findAll())
                .thenReturn(cities);

        List<CityServiceModel> allCities = service.getAll();

        assertEquals(cities.size(), allCities.size());
    }

//    @Test
//    public void getIdByCityNameMethodShouldReturnCorrectId() {
//        String cityName = "City name";
//
//        City city = new City();
//        city.setId("1");
//        city.setName(cityName);
//
//        Mockito.when(mockCityRepository.getByName(cityName))
//                .thenReturn(city);
//
//        String idByCityName = service.getIdByCityName(cityName);
//
//        assertEquals(city.getId(), idByCityName);
//    }

//    @Test
//    public void getIdByCityNameMethodShouldReturnEmptyStringWhenCityDoesNotExist() {
//        String cityName = "City name";
//
//        Mockito.when(mockCityRepository.getByName(cityName))
//                .thenReturn(null);
//
//        String idByCityName = service.getIdByCityName(cityName);
//
//        assertEquals("", idByCityName);
//    }
}
