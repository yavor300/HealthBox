package project.healthbox.service;

import project.healthbox.domain.models.service.AnswerServiceModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;

public interface ConsultationService {
    ConsultationServiceModel save(ConsultationServiceModel model);
    void setDoctorAndUser(ConsultationServiceModel consultationServiceModel, DoctorServiceModel doctorServiceModel, UserServiceModel userServiceModel);
    void setAnswer(ConsultationServiceModel consultationServiceModel, AnswerServiceModel answerServiceModel);
    ConsultationServiceModel getById(String id);
}
