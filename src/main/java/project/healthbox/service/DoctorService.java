package project.healthbox.service;


import project.healthbox.domain.models.service.DoctorServiceModel;

import java.io.IOException;
import java.util.List;

public interface DoctorService {
    DoctorServiceModel completeProfile(DoctorServiceModel doctorServiceModel) throws IOException;

    DoctorServiceModel getById(String id);

    DoctorServiceModel getByEmail(String email);

    List<DoctorServiceModel> getAll();

    void deleteDoctor(String id);

    List<DoctorServiceModel> getAllByGivenCriteria(String specialtyId, String cityId, String firstName, String lastName);
}
