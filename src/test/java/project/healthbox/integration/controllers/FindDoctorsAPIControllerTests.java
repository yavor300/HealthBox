package project.healthbox.integration.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.Specialty;
import project.healthbox.repostory.DoctorRepository;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FindDoctorsAPIControllerTests {
    private static final String ID = "UUID";
    private static final String NON_EXISTENT_UUID = "NON_EXISTENT_UUID";
    private static final String NON_EXISTENT_FIRST_NAME = "NON_EXISTENT_FIRST_NAME";
    private static final String NON_EXISTENT_LAST_NAME = "NON_EXISTENT_LAST_NAME";
    private static final String DOCTOR_1_NAME = "DOCTOR_1";
    private static final String DOCTOR_2_NAME = "DOCTOR_2";
    private static final String DOCTOR_3_NAME = "DOCTOR_3";
    private static final String DOCTOR_3_LAST_NAME = "DOCTOR_3_LAST_NAME";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private DoctorRepository mockDoctorRepository;

    private Doctor doctor1, doctor2, doctor3;

    @Before
    public void init() {
        doctor1 = new Doctor();
        doctor1.setId(ID);
        doctor1.setFirstName(DOCTOR_1_NAME);
        doctor1.setSpecialty(new Specialty() {{
            setId(ID);
        }});

        doctor2 = new Doctor();
        doctor2.setId(ID);
        doctor2.setFirstName(DOCTOR_2_NAME);
        doctor2.setLocation(new City() {{
            setId(ID);
        }});

        doctor3 = new Doctor();
        doctor3.setId(ID);
        doctor3.setFirstName(DOCTOR_3_NAME);
        doctor3.setLastName(DOCTOR_3_LAST_NAME);

        when(mockDoctorRepository.findAllDoctorsByGivenCriteria("", "", "", ""))
                .thenReturn(List.of(doctor1, doctor2, doctor3));

        when(mockDoctorRepository.findAllDoctorsByGivenCriteria(NON_EXISTENT_UUID, NON_EXISTENT_UUID, NON_EXISTENT_FIRST_NAME, NON_EXISTENT_LAST_NAME))
                .thenReturn(List.of());

        when(mockDoctorRepository.findAllDoctorsByGivenCriteria(ID, "", "", ""))
                .thenReturn(List.of(doctor1));

        when(mockDoctorRepository.findAllDoctorsByGivenCriteria("", ID, "", ""))
                .thenReturn(List.of(doctor2));

        when(mockDoctorRepository.findAllDoctorsByGivenCriteria("", "", DOCTOR_3_NAME, DOCTOR_3_LAST_NAME))
                .thenReturn(List.of(doctor3));
    }

    @Test
    @WithMockUser
    public void findDoctors_Should_ReturnCorrectStatusCode() throws Exception {
        mockMvc.perform(get("/findDoctors/specialty_id=&city_id=&first_name=&last_name=")).
                andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void findDoctors_Should_NotFindAnyResults() throws Exception {
        mockMvc.perform(get("/findDoctors/specialty_id={specialtyId}&city_id={cityId}&first_name={firstName}&last_name={lastName}",
                NON_EXISTENT_UUID, NON_EXISTENT_UUID, NON_EXISTENT_FIRST_NAME, NON_EXISTENT_LAST_NAME)).
                andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void findDoctors_Should_FindDoctors_BySpecialtyId() throws Exception {
        this.mockMvc.
                perform(get("/findDoctors/specialty_id={specialtyId}&city_id=&first_name=&last_name=", doctor1.getSpecialty().getId())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.[0].firstName", is(doctor1.getFirstName())));
    }

    @Test
    @WithMockUser
    public void findDoctors_Should_FindDoctors_ByCityId() throws Exception {
        this.mockMvc.
                perform(get("/findDoctors/specialty_id=&city_id={cityId}&first_name=&last_name=", doctor2.getLocation().getId())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.[0].firstName", is(doctor2.getFirstName())));
    }

    @Test
    @WithMockUser
    public void findDoctors_Should_FindDoctors_ByFirstAndLastName() throws Exception {
        this.mockMvc.
                perform(get("/findDoctors/specialty_id=&city_id=&first_name={firstName}&last_name={lastName}",
                        doctor3.getFirstName(), doctor3.getLastName())).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.[0].firstName", is(doctor3.getFirstName()))).
                andExpect(jsonPath("$.[0].lastName", is(doctor3.getLastName())));
    }

    @Test
    @WithMockUser
    public void findDoctors_Should_ReturnAllDoctors() throws Exception {
        mockMvc.
                perform(get("/findDoctors/specialty_id=&city_id=&first_name=&last_name=")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$", hasSize(3))).
                andExpect(jsonPath("$.[0].id", is(doctor1.getId()))).
                andExpect(jsonPath("$.[0].firstName", is(doctor1.getFirstName()))).
                andExpect(jsonPath("$.[1].id", is(doctor2.getId()))).
                andExpect(jsonPath("$.[1].firstName", is(doctor2.getFirstName()))).
                andExpect(jsonPath("$.[2].id", is(doctor3.getId()))).
                andExpect(jsonPath("$.[2].firstName", is(doctor3.getFirstName())));
    }
}