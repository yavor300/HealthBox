package project.healthbox.service;

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
import project.healthbox.repostory.ConsultationRepository;
import project.healthbox.repostory.DoctorRepository;
import project.healthbox.repostory.UserRepository;

import java.util.List;

@Service
public class ConsultationServiceImpl implements ConsultationService {
    private final ConsultationRepository consultationRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    public ConsultationServiceImpl(ConsultationRepository consultationRepository, UserRepository userRepository, DoctorRepository doctorRepository, ModelMapper modelMapper) {
        this.consultationRepository = consultationRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ConsultationServiceModel save(ConsultationServiceModel model) {
        return this.modelMapper.map(this.consultationRepository.saveAndFlush(
                this.modelMapper.map(model, Consultation.class)), ConsultationServiceModel.class);
    }

    @Override
    public void setDoctorAndUser(ConsultationServiceModel consultationServiceModel, DoctorServiceModel doctorServiceModel, UserServiceModel userServiceModel) {
        Consultation consultation = this.modelMapper.map(consultationServiceModel, Consultation.class);
        Doctor doctor = this.modelMapper.map(doctorServiceModel, Doctor.class);
        User user = this.modelMapper.map(userServiceModel, User.class);
        consultation.setDoctor(doctor);
        consultation.setUser(user);
        this.consultationRepository.saveAndFlush(consultation);
    }

    @Override
    public void setAnswer(ConsultationServiceModel consultationServiceModel, AnswerServiceModel answerServiceModel) {
        Consultation consultation = this.modelMapper.map(consultationServiceModel, Consultation.class);
        Answer answer = this.modelMapper.map(answerServiceModel, Answer.class);
        consultation.setAnswer(answer);
        this.consultationRepository.saveAndFlush(consultation);
    }

    @Override
    public ConsultationServiceModel getById(String id) {
        return this.modelMapper.map(this.consultationRepository.getById(id), ConsultationServiceModel.class);
    }
}
