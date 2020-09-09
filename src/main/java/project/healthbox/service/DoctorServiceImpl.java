package project.healthbox.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.repostory.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, ModelMapper modelMapper) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DoctorServiceModel update(DoctorUpdateBindingModel doctorUpdateBindingModel) {
        Doctor doctor = this.doctorRepository.getById(doctorUpdateBindingModel.getId());
        doctor.setLocation(doctorUpdateBindingModel.getLocation());
        doctor.setBiography(doctorUpdateBindingModel.getBiography());
        doctor.setWorkHistory(doctorUpdateBindingModel.getWorkHistory());
        doctor.setEducation(doctorUpdateBindingModel.getEducation());
        return this.modelMapper.map(doctorRepository.saveAndFlush(doctor), DoctorServiceModel.class);
    }
}
