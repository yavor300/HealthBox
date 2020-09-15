package project.healthbox.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.models.binding.ChooseSpecialistBindingModel;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;
import project.healthbox.repostory.DoctorRepository;
import project.healthbox.repostory.SpecialtyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    private final SpecialtyRepository specialtyRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, ModelMapper modelMapper, SpecialtyRepository specialtyRepository) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public DoctorServiceModel update(DoctorUpdateBindingModel doctorUpdateBindingModel) {
        Doctor doctor = this.doctorRepository.getById(doctorUpdateBindingModel.getId());
        doctor.setLocation(doctorUpdateBindingModel.getLocation());
        doctor.setBiography(doctorUpdateBindingModel.getBiography());
        doctor.setWorkHistory(doctorUpdateBindingModel.getWorkHistory());
        doctor.setEducation(doctorUpdateBindingModel.getEducation());
        doctor.setSpecialty(this.specialtyRepository.findByName(doctorUpdateBindingModel.getSpecialty()));
        return this.modelMapper.map(doctorRepository.saveAndFlush(doctor), DoctorServiceModel.class);
    }

    @Override
    public boolean isAccountCompleted(UserLoginServiceModel user) {
        Doctor doctor = this.doctorRepository.getById(user.getId());
        return doctor.getBiography() != null && doctor.getWorkHistory() != null &&
                doctor.getEducation() != null && doctor.getLocation() != null;

    }

    @Override
    public List<DoctorServiceModel> getAllBySpecialtyName(String name) {
        return this.doctorRepository.findBySpecialtyName(name)
                .stream()
                .map(d -> this.modelMapper.map(d, DoctorServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorServiceModel> findAllByGivenCriteria(ChooseSpecialistBindingModel model) {
        if (model.getSpecialty() != null && !model.getSpecialty().trim().isEmpty() && model.getDoctorName().isEmpty() && model.getLocation().isEmpty()) {
            return this.getAllBySpecialtyName(model.getSpecialty());
        }
        return null; //TODO
    }

    @Override
    public DoctorServiceModel getById(String id) {
        return this.modelMapper.map(
                this.doctorRepository.getById(id),
                DoctorServiceModel.class
        );
    }
}
