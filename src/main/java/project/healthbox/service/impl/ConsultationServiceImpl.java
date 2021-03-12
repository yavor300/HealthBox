package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Answer;
import project.healthbox.domain.entities.Consultation;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.User;
import project.healthbox.domain.models.service.AnswerServiceModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.error.ConsultationNotFoundException;
import project.healthbox.repostory.ConsultationRepository;
import project.healthbox.service.ConsultationService;

@Service
@AllArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {
    private final ConsultationRepository consultationRepository;
    private final ModelMapper modelMapper;

    @Override
    public ConsultationServiceModel save(ConsultationServiceModel consultationServiceModel, UserServiceModel userServiceModel, DoctorServiceModel doctorServiceModel) {
        consultationServiceModel.setUser(userServiceModel);
        consultationServiceModel.setDoctor(doctorServiceModel);
        return modelMapper.map(consultationRepository.saveAndFlush(
                modelMapper.map(consultationServiceModel, Consultation.class)), ConsultationServiceModel.class);
    }

    @Override
    public AnswerServiceModel setAnswer(ConsultationServiceModel consultationServiceModel, AnswerServiceModel answerServiceModel) {
        Consultation consultation = modelMapper.map(consultationServiceModel, Consultation.class);
        Answer answer = modelMapper.map(answerServiceModel, Answer.class);
        consultation.setAnswer(answer);
        return modelMapper.map(consultationRepository.saveAndFlush(consultation), AnswerServiceModel.class);
    }

    @Override
    public ConsultationServiceModel getById(String id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundException("Invalid consultation identifier!"));
        return modelMapper.map(consultation, ConsultationServiceModel.class);
    }
}