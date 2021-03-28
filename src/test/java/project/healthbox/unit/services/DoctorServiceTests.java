package project.healthbox.unit.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.entities.Consultation;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.Specialty;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.SpecialtyServiceModel;
import project.healthbox.error.ObjectNotFoundException;
import project.healthbox.events.register.RegisterEventPublisher;
import project.healthbox.repostory.DoctorRepository;
import project.healthbox.service.CityService;
import project.healthbox.service.CloudinaryService;
import project.healthbox.service.DoctorService;
import project.healthbox.service.SpecialtyService;
import project.healthbox.service.impl.DoctorServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class DoctorServiceTests {
    private static final String ID = "UUID";
    private static final String EMAIL = "EMAIL";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String CITY_NAME = "CITY_NAME";
    private static final String IMG_URL = "URL";
    private static final String BIOGRAPHY = "BIOGRAPHY";
    private static final String WORK_HISTORY = "WORK_HISTORY";
    private static final String EDUCATION = "EDUCATION";
    private static final String SPECIALTY_NAME = "SPECIALTY_NAME";

    private DoctorRepository mockDoctorRepository;
    private DoctorService doctorService;
    private SpecialtyService mockSpecialtyService;
    private CityService mockCityService;
    private CloudinaryService mockCloudinaryService;
    private RegisterEventPublisher mockRegisterEventPublisher;

    private Doctor doctor;

    @Before
    public void init() {
        mockDoctorRepository = Mockito.mock(DoctorRepository.class);
        mockSpecialtyService = Mockito.mock(SpecialtyService.class);
        mockCityService = Mockito.mock(CityService.class);
        mockCloudinaryService = Mockito.mock(CloudinaryService.class);
        mockRegisterEventPublisher = Mockito.mock(RegisterEventPublisher.class);
        doctorService = new DoctorServiceImpl(mockDoctorRepository, new ModelMapper(), mockSpecialtyService, mockCityService, mockCloudinaryService, mockRegisterEventPublisher);

        doctor = new Doctor();
        doctor.setId(ID);
        doctor.setEmail(EMAIL);
        doctor.setFirstName(FIRST_NAME);
        doctor.setLastName(LAST_NAME);
        doctor.setLocation(new City() {{
            setName(CITY_NAME);
        }});
        doctor.setBiography(BIOGRAPHY);
        doctor.setWorkHistory(WORK_HISTORY);
        doctor.setEducation(EDUCATION);
        doctor.setSpecialty(new Specialty() {{
            setName(SPECIALTY_NAME);
        }});
        doctor.setConsultations(List.of(new Consultation() {{
            setDoctor(doctor);
        }}));
    }

    @Test
    public void completeProfile_Should_UpdateAndReturn_DoctorServiceModel() throws IOException {
        DoctorServiceModel doctorServiceModel = new DoctorServiceModel();
        doctorServiceModel.setId(ID);
        doctorServiceModel.setLocation(new CityServiceModel() {{
            setName(CITY_NAME);
        }});
        doctorServiceModel.setImage(new MockMultipartFile("MultiPartFile", new byte[]{}));
        doctorServiceModel.setBiography(BIOGRAPHY);
        doctorServiceModel.setWorkHistory(WORK_HISTORY);
        doctorServiceModel.setEducation(EDUCATION);
        doctorServiceModel.setSpecialty(new SpecialtyServiceModel() {{
            setName(SPECIALTY_NAME);
        }});

        DoctorServiceModel result = new DoctorServiceModel();
        result.setId(ID);
        result.setEmail(EMAIL);
        result.setFirstName(FIRST_NAME);
        result.setLastName(LAST_NAME);
        result.setLocation(new CityServiceModel() {{
            setName(CITY_NAME);
        }});
        result.setBiography(BIOGRAPHY);
        result.setWorkHistory(WORK_HISTORY);
        result.setEducation(EDUCATION);
        result.setSpecialty(new SpecialtyServiceModel() {{
            setName(SPECIALTY_NAME);
        }});

        CityServiceModel city = new CityServiceModel();
        city.setName(CITY_NAME);

        SpecialtyServiceModel specialty = new SpecialtyServiceModel();
        specialty.setName(SPECIALTY_NAME);

        Mockito.when(mockDoctorRepository.findById(ID))
                .thenReturn(Optional.of(doctor));

        Mockito.when(mockCityService.getByName(CITY_NAME))
                .thenReturn(city);

        Mockito.when(mockCloudinaryService.uploadImage(doctorServiceModel.getImage()))
                .thenReturn(IMG_URL);

        Mockito.when(mockSpecialtyService.getByName(SPECIALTY_NAME))
                .thenReturn(specialty);

        Mockito.when(mockDoctorRepository.saveAndFlush(any(Doctor.class)))
                .thenReturn(doctor);


        DoctorServiceModel actual = doctorService.completeProfile(doctorServiceModel);

        assertEquals(result.getId(), actual.getId());
        assertEquals(result.getBiography(), actual.getBiography());
        assertEquals(IMG_URL, actual.getImageUrl());
        assertEquals(result.getSpecialty().getName(), actual.getSpecialty().getName());
        assertEquals(result.getLocation().getName(), actual.getLocation().getName());
        assertEquals(result.getWorkHistory(), actual.getWorkHistory());
        assertEquals(result.getEducation(), actual.getEducation());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void completeProfile_Should_ThrowAnException() throws IOException {
        DoctorServiceModel doctorServiceModel = new DoctorServiceModel();
        doctorServiceModel.setId(ID);

        Mockito.when(mockDoctorRepository.findById(ID))
                .thenReturn(Optional.empty());


        doctorService.completeProfile(doctorServiceModel);
    }

    @Test
    public void getAll_Should_Return_CollectionOfDoctors() {
        Mockito.when(mockDoctorRepository.findAll())
                .thenReturn(List.of(doctor));

        List<DoctorServiceModel> result = doctorService.getAll();
        DoctorServiceModel doctorServiceModel = result.get(0);

        assertEquals(1, result.size());
        assertEquals(doctor.getId(), doctorServiceModel.getId());
        assertEquals(doctor.getBiography(), doctorServiceModel.getBiography());
        assertEquals(doctor.getSpecialty().getName(), doctorServiceModel.getSpecialty().getName());
        assertEquals(doctor.getLocation().getName(), doctorServiceModel.getLocation().getName());
        assertEquals(doctor.getWorkHistory(), doctorServiceModel.getWorkHistory());
        assertEquals(doctor.getEducation(), doctorServiceModel.getEducation());
    }

    @Test
    public void deleteDoctor_Should_ExecuteCorrectly() {
        Mockito.when(mockDoctorRepository.findById(ID))
                .thenReturn(Optional.of(doctor));

        doctorService.deleteDoctor(ID);

        Mockito.verify(mockDoctorRepository, times(1)).delete(doctor);
        assertNull(doctor.getConsultations().get(0).getDoctor());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteDoctor_Should_ThrownAnError() {


        Mockito.when(mockDoctorRepository.findById(ID))
                .thenReturn(Optional.empty());

        doctorService.deleteDoctor(ID);
    }

    @Test
    public void getById_Should_Return_CorrectDoctor() {
        Mockito.when(mockDoctorRepository.findById(ID))
                .thenReturn(Optional.of(doctor));

        DoctorServiceModel doctorServiceById = doctorService.getById(ID);

        assertEquals(doctor.getId(), doctorServiceById.getId());
        assertEquals(doctor.getFirstName(), doctorServiceById.getFirstName());
        assertEquals(doctor.getLastName(), doctorServiceById.getLastName());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getById_Should_ThrowAnException_If_DoctorDoesNotExist() {
        Mockito.when(mockDoctorRepository.findById(ID))
                .thenReturn(Optional.empty());

        doctorService.getById(ID);
    }

    @Test
    public void getByEmail_Should_Return_CorrectDoctor() {
        Mockito.when(mockDoctorRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(doctor));

        DoctorServiceModel doctorServiceByEmail = doctorService.getByEmail(EMAIL);

        assertEquals(doctor.getId(), doctorServiceByEmail.getId());
        assertEquals(doctor.getFirstName(), doctorServiceByEmail.getFirstName());
        assertEquals(doctor.getLastName(), doctorServiceByEmail.getLastName());
        assertEquals(doctor.getEmail(), doctorServiceByEmail.getEmail());
    }

    @Test
    public void getByEmail_Should_Return_NULL() {
        Mockito.when(mockDoctorRepository.findByEmail(EMAIL))
                .thenReturn(Optional.empty());

        DoctorServiceModel doctorServiceByEmail = doctorService.getByEmail(EMAIL);

        assertNull(doctorServiceByEmail);
    }

    @Test
    public void getAllByGivenCriteria_Should_Return_CollectionOfDoctors() {
        Mockito.when(mockDoctorRepository.findAllDoctorsByGivenCriteria(ID, ID, FIRST_NAME, LAST_NAME))
                .thenReturn(List.of(doctor));

        List<DoctorServiceModel> result = doctorService.getAllByGivenCriteria(ID, ID, FIRST_NAME, LAST_NAME);
        DoctorServiceModel doctorServiceModel = result.get(0);

        assertEquals(1, result.size());
        assertEquals(doctor.getId(), doctorServiceModel.getId());
        assertEquals(doctor.getBiography(), doctorServiceModel.getBiography());
        assertEquals(doctor.getSpecialty().getName(), doctorServiceModel.getSpecialty().getName());
        assertEquals(doctor.getLocation().getName(), doctorServiceModel.getLocation().getName());
        assertEquals(doctor.getWorkHistory(), doctorServiceModel.getWorkHistory());
        assertEquals(doctor.getEducation(), doctorServiceModel.getEducation());
    }
}