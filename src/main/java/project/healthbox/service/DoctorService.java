package project.healthbox.service;

import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.service.DoctorServiceModel;

public interface DoctorService {
    DoctorServiceModel update(DoctorUpdateBindingModel doctorUpdateBindingModel);
}
