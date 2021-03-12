package project.healthbox.integration.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.Specialty;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.repostory.CityRepository;
import project.healthbox.repostory.DoctorRepository;
import project.healthbox.repostory.SpecialtyRepository;
import project.healthbox.service.CloudinaryService;
import project.healthbox.service.DoctorService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DoctorServiceTest {
    @Autowired
    private DoctorService service;

    @MockBean
    private DoctorRepository mockRepository;

    @MockBean
    CityRepository cityRepository;

    @MockBean
    CloudinaryService cloudinaryService;

    @MockBean
    SpecialtyRepository specialtyRepository;

//    @Test
//    public void updateShouldUpdateTheEntityCorrectly() throws IOException {
//        DoctorUpdateBindingModel doctorUpdateBindingModel = new DoctorUpdateBindingModel();
//        doctorUpdateBindingModel.setId("id");
//        doctorUpdateBindingModel.setLocation("Location");
//        doctorUpdateBindingModel.setImage(null);
//        doctorUpdateBindingModel.setBiography("Bio");
//        doctorUpdateBindingModel.setEducation("Education");
//        doctorUpdateBindingModel.setWorkHistory("Work");
//
//        City city = new City();
//        city.setName("City name");
//
//        Specialty specialty = new Specialty();
//        specialty.setName("Name");
//
//        Doctor doctor = new Doctor();
//        doctor.setId(doctorUpdateBindingModel.getId());
//        doctor.setLocation(city);
//        doctor.setImageUrl("img-url");
//        doctor.setEducation(doctorUpdateBindingModel.getEducation());
//        doctor.setBiography(doctorUpdateBindingModel.getBiography());
//        doctor.setWorkHistory(doctor.getWorkHistory());
//        doctor.setSpecialty(specialty);
//
//        Mockito.when(mockRepository.getById(doctorUpdateBindingModel.getId()))
//                .thenReturn(doctor);
//
//        Mockito.when(cityRepository.getByName(doctorUpdateBindingModel.getLocation()))
//                .thenReturn(city);
//
//        Mockito.when(cloudinaryService.uploadImage(null))
//                .thenReturn("img-url");
//
//        Mockito.when(specialtyRepository.findByName(doctorUpdateBindingModel.getSpecialty()))
//                .thenReturn(specialty);
//
//        Mockito.when(mockRepository.saveAndFlush(doctor))
//                .thenReturn(doctor);
//
//
//        DoctorServiceModel update = service.update(doctorUpdateBindingModel);
//
//        assertEquals(doctor.getBiography(), update.getBiography());
//        assertEquals(doctor.getId(), update.getId());
//        assertEquals("img-url", update.getImageUrl());
//        assertEquals(doctor.getSpecialty().getName(), update.getSpecialty().getName());
//        assertEquals(doctor.getLocation().getName(), update.getLocation().getName());
//        assertEquals(doctor.getWorkHistory(), update.getWorkHistory());
//        assertEquals(doctor.getEducation(), update.getEducation());
//    }

    @Test
    public void getAllShouldReturnCollectionOfServiceModels() {
        List<Doctor> doctors = new ArrayList<>();

        Mockito.when(mockRepository.findAll())
                .thenReturn(doctors);

        List<DoctorServiceModel> all = service.getAll();

        assertEquals(doctors.size(), all.size());
    }

//    @Test
//    public void getByIdMethodShouldReturnCorrectEntity() {
//        String id = "id";
//
//        Doctor doctor = new Doctor();
//        doctor.setId(id);
//
//        Mockito.when(mockRepository.getById(id))
//                .thenReturn(doctor);
//
//        DoctorServiceModel byId = service.getById(id);
//
//        assertEquals(doctor.getId(), byId.getId());
//    }

    @Test
    public void getByEmailShouldReturnCorrectly() {
        String email = "email";

        Doctor doctor = new Doctor();
        doctor.setEmail(email);

        Mockito.when(mockRepository.findByEmail(email))
                .thenReturn(Optional.of(doctor));

        DoctorServiceModel result = service.getByEmail(email);

        assertEquals(doctor.getEmail(), result.getEmail());
    }

    @Test
    public void getByEmailShouldReturnNull() {
        String email = "email";

        Mockito.when(mockRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        DoctorServiceModel result = service.getByEmail(email);

        assertNull(result);
    }
}

