package project.healthbox.integration.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import project.healthbox.domain.entities.*;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.repostory.ConsultationRepository;
import project.healthbox.service.ConsultationService;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ConsultationServiceTest {
    @Autowired
    private ConsultationService service;

    @MockBean
    private ConsultationRepository mockRepository;

//    @Test
//    public void getByIdMethodShouldReturnCorrectly() {
//        String id = "id";
//        Consultation consultation = new Consultation();
//        consultation.setId(id);
//
//        Mockito.when(mockRepository.getById(id)).
//                thenReturn(consultation);
//
//        ConsultationServiceModel consultationServiceModel = service.getById(id);
//
//        assertEquals(id, consultationServiceModel.getId());
//    }

}
