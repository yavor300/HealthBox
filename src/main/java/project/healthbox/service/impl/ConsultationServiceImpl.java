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
    public ConsultationServiceModel save(ConsultationServiceModel model) {
        return modelMapper.map(consultationRepository.saveAndFlush(
                modelMapper.map(model, Consultation.class)), ConsultationServiceModel.class);
    }

    //TODO SEPARATE IN TWO METHODS ?
    //TODO RETURN THE SERVICE MODELS ?
    @Override
    public void setDoctorAndUser(ConsultationServiceModel consultationServiceModel, DoctorServiceModel doctorServiceModel, UserServiceModel userServiceModel) {
        Consultation consultation = modelMapper.map(consultationServiceModel, Consultation.class);
        Doctor doctor = modelMapper.map(doctorServiceModel, Doctor.class);
        User user = modelMapper.map(userServiceModel, User.class);
        consultation.setDoctor(doctor);
        consultation.setUser(user);
        consultationRepository.saveAndFlush(consultation);
    }

    //TODO RETURN ANSWERSERIVEMODEL ?
    @Override
    public void setAnswer(ConsultationServiceModel consultationServiceModel, AnswerServiceModel answerServiceModel) {
        Consultation consultation = modelMapper.map(consultationServiceModel, Consultation.class);
        Answer answer = modelMapper.map(answerServiceModel, Answer.class);
        consultation.setAnswer(answer);
        consultationRepository.saveAndFlush(consultation);
    }

    @Override
    public ConsultationServiceModel getById(String id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundException("Invalid consultation identifier!"));
        return modelMapper.map(consultation, ConsultationServiceModel.class);
    }
}