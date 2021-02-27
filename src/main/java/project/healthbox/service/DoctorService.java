package project.healthbox.service;


import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.service.DoctorServiceModel;

import java.io.IOException;
import java.util.List;

public interface DoctorService {
    DoctorServiceModel update(DoctorUpdateBindingModel doctorUpdateBindingModel) throws IOException;

    DoctorServiceModel getById(String id);

    DoctorServiceModel getByEmail(String email);

    List<DoctorServiceModel> getAll();

    void deleteDoctor(String id);
}
