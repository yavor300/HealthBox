package project.healthbox.integration.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.Specialty;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;
import project.healthbox.error.DoctorsNotFoundException;
import project.healthbox.repostory.CityRepository;
import project.healthbox.repostory.DoctorRepository;
import project.healthbox.repostory.SpecialtyRepository;
import project.healthbox.service.CityService;
import project.healthbox.service.CloudinaryService;
import project.healthbox.service.DoctorService;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void isAccountCompletedShouldReturnTrue() {
        UserLoginServiceModel userLoginServiceModel = new UserLoginServiceModel();
        userLoginServiceModel.setId("id");

        Doctor doctor = new Doctor();
        doctor.setBiography("Bio.");
        doctor.setWorkHistory("Work History.");
        doctor.setEducation("Education.");
        doctor.setLocation(new City());

        Mockito.when(mockRepository.getById(userLoginServiceModel.getId()))
                .thenReturn(doctor);

        assertTrue(service.isAccountCompleted(userLoginServiceModel));
    }

    @Test
    public void isAccountCompletedShouldReturnFalse() {
        UserLoginServiceModel userLoginServiceModel = new UserLoginServiceModel();
        userLoginServiceModel.setId("id");

        Doctor doctor = new Doctor();
        doctor.setBiography(null);
        doctor.setWorkHistory("Work History.");
        doctor.setEducation("Education.");
        doctor.setLocation(new City());

        Mockito.when(mockRepository.getById(userLoginServiceModel.getId()))
                .thenReturn(doctor);

        assertFalse(service.isAccountCompleted(userLoginServiceModel));
    }

    @Test
    public void updateShouldUpdateTheEntityCorrectly() throws IOException {
        DoctorUpdateBindingModel doctorUpdateBindingModel = new DoctorUpdateBindingModel();
        doctorUpdateBindingModel.setId("id");
        doctorUpdateBindingModel.setLocation("Location");
        doctorUpdateBindingModel.setImage(null);
        doctorUpdateBindingModel.setBiography("Bio");
        doctorUpdateBindingModel.setEducation("Education");
        doctorUpdateBindingModel.setWorkHistory("Work");

        City city = new City();
        city.setName("City name");

        Specialty specialty = new Specialty();
        specialty.setName("Name");

        Doctor doctor = new Doctor();
        doctor.setId(doctorUpdateBindingModel.getId());
        doctor.setLocation(city);
        doctor.setImageUrl("img-url");
        doctor.setEducation(doctorUpdateBindingModel.getEducation());
        doctor.setBiography(doctorUpdateBindingModel.getBiography());
        doctor.setWorkHistory(doctor.getWorkHistory());
        doctor.setSpecialty(specialty);

        Mockito.when(mockRepository.getById(doctorUpdateBindingModel.getId()))
                .thenReturn(doctor);

        Mockito.when(cityRepository.getByName(doctorUpdateBindingModel.getLocation()))
                .thenReturn(city);

        Mockito.when(cloudinaryService.uploadImage(null))
                .thenReturn("img-url");

        Mockito.when(specialtyRepository.findByName(doctorUpdateBindingModel.getSpecialty()))
                .thenReturn(specialty);

        Mockito.when(mockRepository.saveAndFlush(doctor))
                .thenReturn(doctor);


        DoctorServiceModel update = service.update(doctorUpdateBindingModel);

        assertEquals(doctor.getBiography(), update.getBiography());
        assertEquals(doctor.getId(), update.getId());
        assertEquals("img-url", update.getImageUrl());
        assertEquals(doctor.getSpecialty().getName(), update.getSpecialty().getName());
        assertEquals(doctor.getLocation().getName(), update.getLocation().getName());
        assertEquals(doctor.getWorkHistory(), update.getWorkHistory());
        assertEquals(doctor.getEducation(), update.getEducation());
    }

    @Test
    public void getAllShouldReturnCollectionOfServiceModels() {
        List<Doctor> doctors = new ArrayList<>();

        Mockito.when(mockRepository.findAll())
                .thenReturn(doctors);

        List<DoctorServiceModel> all = service.getAll();

        assertEquals(doctors.size(), all.size());
    }

    @Test
    public void findAllByGivenCriteriaShouldReturnCollection() {
        String specialtyId = null;
        String doctorName = null;
        String cityId = "1";

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor());

        Mockito.when(mockRepository.findAllByLocationId(cityId))
                .thenReturn(doctors);

        List<DoctorServiceModel> allByGivenCriteria = service.findAllByGivenCriteria(specialtyId, cityId, doctorName);

        assertEquals(allByGivenCriteria.size(), allByGivenCriteria.size());
    }

    @Test(expected = DoctorsNotFoundException.class)
    public void findAllByGivenCriteriaShouldThrowException() {
        String specialtyId = null;
        String doctorName = null;
        String cityId = "1";

        List<Doctor> doctors = new ArrayList<>();

        Mockito.when(mockRepository.findAllByLocationId(cityId))
                .thenReturn(doctors);

        List<DoctorServiceModel> allByGivenCriteria = service.findAllByGivenCriteria(specialtyId, cityId, doctorName);
    }

    @Test
    public void getByIdMethodShouldReturnCorrectEntity() {
        String id = "id";

        Doctor doctor = new Doctor();
        doctor.setId(id);

        Mockito.when(mockRepository.getById(id))
                .thenReturn(doctor);

        DoctorServiceModel byId = service.getById(id);

        assertEquals(doctor.getId(), byId.getId());
    }

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

    @Test
    public void findAllByFirstNameAndLastNameShouldReturnCorrectly() {
        String firstName = "first name";
        String lastName = "last name";

        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);

        Mockito.when(mockRepository.findAllByFirstNameAndLastName(firstName, lastName))
                .thenReturn(doctors);

        List<DoctorServiceModel> allByFirstNameAndLastName = service.findAllByFirstNameAndLastName(firstName, lastName);

        assertEquals(doctors.size(), allByFirstNameAndLastName.size());
    }

    @Test
    public void findAllByFirstNameAndLastNameShouldReturnNull() {
        String firstName = "first name";
        String lastName = "last name";

        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);

        List<Doctor> doctors = new ArrayList<>();


        Mockito.when(mockRepository.findAllByFirstNameAndLastName(firstName, lastName))
                .thenReturn(doctors);

        assertNull(service.findAllByFirstNameAndLastName(firstName, lastName));
    }

    @Test
    public void findAllByLocationIdShouldReturnCorrectly() {
        String id = "id";

        Doctor doctor = new Doctor();

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);

        Mockito.when(mockRepository.findAllByLocationId(id))
                .thenReturn(doctors);

        List<DoctorServiceModel> allBySpecialtyId = service.findAllByLocationId(id);

        assertEquals(doctors.size(), allBySpecialtyId.size());
    }

    @Test
    public void findAllByLocationShouldReturnNull() {
        String id = "id";

        List<Doctor> doctors = new ArrayList<>();

        Mockito.when(mockRepository.findAllByLocationId(id))
                .thenReturn(doctors);

        assertNull(service.findAllByLocationId(id));
    }

    @Test
    public void findAllBySpecialtyIdShouldReturnCorrectly() {
        String id = "id";

        Doctor doctor = new Doctor();

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);

        Mockito.when(mockRepository.findAllBySpecialtyId(id))
                .thenReturn(doctors);

        List<DoctorServiceModel> allBySpecialtyId = service.findAllBySpecialtyId(id);

        assertEquals(doctors.size(), allBySpecialtyId.size());
    }

    @Test
    public void findAllBySpecialtyIdShouldReturnNull() {
        String id = "id";

        List<Doctor> doctors = new ArrayList<>();

        Mockito.when(mockRepository.findAllBySpecialtyId(id))
                .thenReturn(doctors);

        assertNull(service.findAllBySpecialtyId(id));
    }

    @Test
    public void findAllByLocationIdAndFirstNameAndLastName_Should_ReturnCorrectly() {
        String id = "id";
        String firstName = "first name";
        String lastName = "last name";

        Doctor doctor = new Doctor();

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);

        Mockito.when(mockRepository.findAllByLocationIdAndFirstNameAndLastName(id, firstName, lastName))
                .thenReturn(doctors);

        List<DoctorServiceModel> result = service.findAllByLocationIdAndFirstNameAndLastName(id, firstName, lastName);

        assertEquals(doctors.size(), result.size());
    }

    @Test
    public void findAllByLocationIdAndFirstNameAndLastName_Should_ReturnNull() {
        String id = "id";
        String firstName = "first name";
        String lastName = "last name";

        List<Doctor> doctors = new ArrayList<>();

        Mockito.when(mockRepository.findAllByLocationIdAndFirstNameAndLastName(id, firstName, lastName))
                .thenReturn(doctors);

        assertNull(service.findAllByLocationIdAndFirstNameAndLastName(id, firstName, lastName));
    }

    @Test
    public void findAllBySpecialtyIdAndFirstNameAndLastName_Should_ReturnCorrectly() {
        String id = "id";
        String firstName = "first name";
        String lastName = "last name";

        Doctor doctor = new Doctor();

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);

        Mockito.when(mockRepository.findAllBySpecialtyIdAndFirstNameAndLastName(id, firstName, lastName))
                .thenReturn(doctors);

        List<DoctorServiceModel> result = service.findAllBySpecialtyIdAndFirstNameAndLastName(id, firstName, lastName);

        assertEquals(doctors.size(), result.size());
    }

    @Test
    public void findAllBySpecialtyIdAndFirstNameAndLastName_Should_ReturnNull() {
        String id = "id";
        String firstName = "first name";
        String lastName = "last name";

        List<Doctor> doctors = new ArrayList<>();

        Mockito.when(mockRepository.findAllBySpecialtyIdAndFirstNameAndLastName(id, firstName, lastName))
                .thenReturn(doctors);

        List<DoctorServiceModel> result = service.findAllBySpecialtyIdAndFirstNameAndLastName(id, firstName, lastName);

        assertNull(result);
    }

    @Test
    public void findAllBySpecialtyIdAndLocationId_Should_ReturnCorrectly() {
        String idSpecialty = "idSpecialty";
        String idLocation = "locationId";

        Doctor doctor = new Doctor();

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);

        Mockito.when(mockRepository.findAllBySpecialtyIdAndLocationId(idSpecialty, idLocation))
                .thenReturn(doctors);

        List<DoctorServiceModel> result = service.findAllBySpecialtyIdAndLocationId(idSpecialty, idLocation);

        assertEquals(doctors.size(), result.size());
    }

    @Test
    public void findAllBySpecialtyIdAndLocationId_Should_ReturnNull() {
        String idSpecialty = "idSpecialty";
        String idLocation = "locationId";

        List<Doctor> doctors = new ArrayList<>();

        Mockito.when(mockRepository.findAllBySpecialtyIdAndLocationId(idSpecialty, idLocation))
                .thenReturn(doctors);

        List<DoctorServiceModel> result = service.findAllBySpecialtyIdAndLocationId(idSpecialty, idLocation);

        assertNull(result);
    }

    @Test
    public void findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName_Should_ReturnCorrectly() {
        String idSpecialty = "idSpecialty";
        String idLocation = "locationId";
        String firstName = "first name";
        String lastName = "last name";

        Doctor doctor = new Doctor();

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);

        Mockito.when(mockRepository.findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName(idSpecialty, idLocation, firstName, lastName))
                .thenReturn(doctors);

        List<DoctorServiceModel> result = service.findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName(idSpecialty, idLocation, firstName, lastName);

        assertEquals(doctors.size(), result.size());
    }

    @Test
    public void findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName_Should_ReturnNull() {
        String idSpecialty = "idSpecialty";
        String idLocation = "locationId";
        String firstName = "first name";
        String lastName = "last name";

        List<Doctor> doctors = new ArrayList<>();

        Mockito.when(mockRepository.findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName(idSpecialty, idLocation, firstName, lastName))
                .thenReturn(doctors);

        List<DoctorServiceModel> result = service.findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName(idSpecialty, idLocation, firstName, lastName);

        assertNull(result);
    }

}

