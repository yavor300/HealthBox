package project.healthbox.service;


import project.healthbox.domain.models.binding.ChooseSpecialistBindingModel;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;

import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;

import java.util.List;

public interface DoctorService {
    DoctorServiceModel update(DoctorUpdateBindingModel doctorUpdateBindingModel);
    boolean isAccountCompleted(UserLoginServiceModel user);
    List<DoctorServiceModel> getAllBySpecialtyName(String name);
    List<DoctorServiceModel> findAllByGivenCriteria(ChooseSpecialistBindingModel model);
    DoctorServiceModel getById(String id);
}
