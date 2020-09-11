package project.healthbox.service;


import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;

import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;

public interface DoctorService {
    DoctorServiceModel update(DoctorUpdateBindingModel doctorUpdateBindingModel);
    boolean isAccountCompleted(UserLoginServiceModel user);
}
