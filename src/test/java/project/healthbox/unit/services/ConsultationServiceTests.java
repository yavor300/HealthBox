package project.healthbox.unit.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import project.healthbox.domain.entities.Answer;
import project.healthbox.domain.entities.Consultation;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.User;
import project.healthbox.domain.models.service.AnswerServiceModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.error.ObjectNotFoundException;
import project.healthbox.repostory.ConsultationRepository;
import project.healthbox.service.ConsultationService;
import project.healthbox.service.impl.ConsultationServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class ConsultationServiceTests {
    private final static String ID = "UUID";
    private final static String PROBLEM_ANSWER = "PROBLEM_ANSWER";

    private ConsultationService consultationService;

    private ConsultationRepository mockConsultationRepository;

    @Before
    public void init() {
        mockConsultationRepository = Mockito.mock(ConsultationRepository.class);
        consultationService = new ConsultationServiceImpl(mockConsultationRepository, new ModelMapper());
    }

    @Test
    public void save_Should_Save_And_Return_ConsultationServiceModel() {
        Consultation consultation = new Consultation();
        consultation.setId(ID);
        consultation.setDoctor(new Doctor(){{
            setId(ID);
        }});
        consultation.setUser(new User(){{
            setId(ID);
        }});

        ConsultationServiceModel expected = new ConsultationServiceModel();
        expected.setId(ID);
        expected.setDoctor(new DoctorServiceModel(){{
            setId(ID);
        }});
        expected.setUser(new UserServiceModel(){{
            setId(ID);
        }});

        ConsultationServiceModel consultationServiceModel = new ConsultationServiceModel();
        consultationServiceModel.setId(ID);

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setId(ID);

        DoctorServiceModel doctorServiceModel = new DoctorServiceModel();
        doctorServiceModel.setId(ID);

        Mockito.when(mockConsultationRepository.saveAndFlush(any(Consultation.class)))
                .thenReturn(consultation);

        ConsultationServiceModel result = consultationService.save(consultationServiceModel, userServiceModel, doctorServiceModel);

        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getUser().getId(), result.getUser().getId());
        Assert.assertEquals(expected.getDoctor().getId(), result.getDoctor().getId());
    }

    @Test
    public void setAnswer_Should_Set_And_Return_UpdatedConsultationServiceModel() {
        Consultation consultation = new Consultation();
        consultation.setId(ID);
        consultation.setAnswer(new Answer(){{
            setId(ID);
            setProblemAnswer(PROBLEM_ANSWER);
        }});

        ConsultationServiceModel expected = new ConsultationServiceModel();
        expected.setId(ID);
        expected.setAnswer(new AnswerServiceModel(){{
            setId(ID);
            setProblemAnswer(PROBLEM_ANSWER);
        }});

        ConsultationServiceModel consultationServiceModel = new ConsultationServiceModel();
        consultationServiceModel.setId(ID);

        AnswerServiceModel answerServiceModel = new AnswerServiceModel();
        answerServiceModel.setId(ID);

        Mockito.when(mockConsultationRepository.saveAndFlush(any(Consultation.class)))
                .thenReturn(consultation);

        ConsultationServiceModel result = consultationService.setAnswer(consultationServiceModel, answerServiceModel);

        Assert.assertEquals(expected.getId(), result.getId());
        Assert.assertEquals(expected.getAnswer().getProblemAnswer(), result.getAnswer().getProblemAnswer());
        Assert.assertEquals(expected.getAnswer().getId(), result.getAnswer().getId());
    }

    @Test
    public void getById_Should_Return_CorrectConsultation() {
        Consultation consultation = new Consultation();
        consultation.setId(ID);

        Mockito.when(mockConsultationRepository.findById(ID))
                .thenReturn(Optional.of(consultation));

        ConsultationServiceModel consultationServiceModel = consultationService.getById(ID);

        assertEquals(consultation.getId(), consultationServiceModel.getId());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getById_Should_ThrowAnException_If_ConsultationDoesNotExist() {
        Mockito.when(mockConsultationRepository.findById(ID))
                .thenReturn(Optional.empty());

        consultationService.getById(ID);
    }
}