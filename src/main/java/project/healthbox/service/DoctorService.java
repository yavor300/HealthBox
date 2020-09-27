package project.healthbox.service;


import project.healthbox.domain.models.binding.ChooseSpecialistBindingModel;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;

import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    DoctorServiceModel update(DoctorUpdateBindingModel doctorUpdateBindingModel) throws IOException;

    DoctorServiceModel getById(String id);

    DoctorServiceModel getByEmail(String email);

    List<DoctorServiceModel> findAllByGivenCriteria(String specialtyId, String locationId, String doctorName);

    List<DoctorServiceModel> findAllByFirstNameAndLastName(String firstName, String lastName);

    List<DoctorServiceModel> findAllByLocationId(String locationId);

    List<DoctorServiceModel> findAllBySpecialtyId(String specialtyId);

    List<DoctorServiceModel> findAllByLocationIdAndFirstNameAndLastName(String locationId, String firstName, String lastName);

    List<DoctorServiceModel> findAllBySpecialtyIdAndFirstNameAndLastName(String specialtyId, String firstName, String lastName);

    List<DoctorServiceModel> findAllBySpecialtyIdAndLocationId(String specialtyId, String locationId);

    List<DoctorServiceModel> findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName(String specialtyId, String locationId, String firstName, String lastName);

    boolean isAccountCompleted(UserLoginServiceModel user);

    List<DoctorServiceModel> getAll();

    void deleteDoctor(String id);
}
