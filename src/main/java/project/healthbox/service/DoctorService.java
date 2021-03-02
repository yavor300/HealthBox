package project.healthbox.service;


import project.healthbox.domain.models.service.DoctorServiceModel;

import java.io.IOException;
import java.util.List;

public interface DoctorService {
    DoctorServiceModel update(DoctorServiceModel doctorServiceModel) throws IOException;

    DoctorServiceModel getById(String id);

    DoctorServiceModel getByEmail(String email);

    List<DoctorServiceModel> getAll();

    void deleteDoctor(String id);
}
