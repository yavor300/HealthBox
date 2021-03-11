package project.healthbox.integration.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import project.healthbox.domain.entities.Specialty;
import project.healthbox.domain.models.service.SpecialtyServiceModel;
import project.healthbox.repostory.SpecialtyRepository;
import project.healthbox.service.SpecialtyService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
@RunWith(SpringRunner.class)
public class SpecialtyServiceTest {
    @Autowired
    private SpecialtyService service;

    @MockBean
    private SpecialtyRepository mockSpecialtyRepository;

    @Test
    public void getAllShouldReturnCollectionOfServiceModels() {
        List<Specialty> specialties = new ArrayList<>();
        specialties.add(new Specialty());

        Mockito.when(mockSpecialtyRepository.findAll())
                .thenReturn(specialties);

        List<SpecialtyServiceModel> all = service.getAll();

        assertEquals(specialties.size(), all.size());
    }

//    @Test
//    public void findByNameShouldReturnCorrectly() {
//        String name = "name";
//
//        Specialty specialty = new Specialty();
//        specialty.setName(name);
//
//        Mockito.when(mockSpecialtyRepository.findByName(name))
//                .thenReturn(specialty);
//
//        SpecialtyServiceModel result = service.getByName(name);
//
//        assertEquals(specialty.getName(), result.getName());
//    }

    @Test
    public void findByNameShouldReturnNull() {
        String name = "name";

        Mockito.when(mockSpecialtyRepository.findByName(name))
                .thenReturn(null);

        SpecialtyServiceModel byName = service.getByName(name);

        assertNull(byName);
    }

//    @Test
//    public void getIdBySpecialtyNameMethod_Should_ReturnCorrectId() {
//        String specialtyName = "name";
//
//        Specialty specialty = new Specialty();
//        specialty.setId("1");
//        specialty.setName(specialtyName);
//
//        Mockito.when(mockSpecialtyRepository.findByName(specialtyName))
//                .thenReturn(specialty);
//
//        String idBySpecialtyName = service.getIdBySpecialtyName(specialtyName);
//
//        assertEquals(specialty.getId(), idBySpecialtyName);
//    }

//    @Test
//    public void  getIdBySpecialtyNameMethod_Should_Return_EmptyString_WhenSpecialtyDoesNotExist() {
//        String specialtyName = "name";
//
//        Mockito.when(mockSpecialtyRepository.findByName(specialtyName))
//                .thenReturn(null);
//
//        String idBySpecialtyName = service.getIdBySpecialtyName(specialtyName);
//
//        assertEquals("", idBySpecialtyName);
//    }

    @Test
    public void getByIdMethodShouldReturnCorrectEntity() {
        String id = "id";

        Specialty specialty = new Specialty();
        specialty.setId(id);

        Mockito.when(mockSpecialtyRepository.getById(id))
                .thenReturn(specialty);

        SpecialtyServiceModel byId = service.getById(id);

        assertEquals(specialty.getId(), byId.getId());
    }
}

