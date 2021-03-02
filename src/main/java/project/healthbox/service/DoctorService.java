package project.healthbox.service;


import org.springframework.web.multipart.MultipartFile;
import project.healthbox.domain.models.service.DoctorServiceModel;

import java.io.IOException;
import java.util.List;

public interface DoctorService {
    DoctorServiceModel update(DoctorServiceModel doctorServiceModel, MultipartFile multipartFile) throws IOException;

    DoctorServiceModel getById(String id);

    DoctorServiceModel getByEmail(String email);

    List<DoctorServiceModel> getAll();

    void deleteDoctor(String id);
}
