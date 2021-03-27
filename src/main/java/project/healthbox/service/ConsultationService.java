package project.healthbox.service;

import project.healthbox.domain.models.service.AnswerServiceModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;

public interface ConsultationService {
    ConsultationServiceModel save(ConsultationServiceModel consultationServiceModel, UserServiceModel userServiceModel, DoctorServiceModel doctorServiceModel);

    ConsultationServiceModel getById(String id);

    ConsultationServiceModel setAnswer(ConsultationServiceModel consultationServiceModel, AnswerServiceModel answerServiceModel);
}
