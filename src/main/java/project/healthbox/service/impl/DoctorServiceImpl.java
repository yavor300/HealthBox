package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.Specialty;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.error.ObjectNotFoundException;
import project.healthbox.repostory.DoctorRepository;
import project.healthbox.service.CityService;
import project.healthbox.service.CloudinaryService;
import project.healthbox.service.DoctorService;
import project.healthbox.service.SpecialtyService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    private final SpecialtyService specialtyService;
    private final CityService cityService;
    private final CloudinaryService cloudinaryService;


    @Override
    public DoctorServiceModel completeProfile(DoctorServiceModel doctorServiceModel) throws IOException {
        Doctor doctor = doctorRepository.findById(doctorServiceModel.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Invalid doctor identifier!"));

        City city = modelMapper.map(cityService.getByName(doctorServiceModel.getLocation().getName()), City.class);

        doctor.setLocation(city);
        doctor.setImageUrl(cloudinaryService.uploadImage(doctorServiceModel.getImage()));
        doctor.setBiography(doctorServiceModel.getBiography());
        doctor.setWorkHistory(doctorServiceModel.getWorkHistory());
        doctor.setEducation(doctorServiceModel.getEducation());
        doctor.setSpecialty(modelMapper.map(
                specialtyService.getByName(doctorServiceModel.getSpecialty().getName()),
                Specialty.class));

        return modelMapper.map(doctorRepository.saveAndFlush(doctor), DoctorServiceModel.class);
    }

    @Override
    public List<DoctorServiceModel> getAll() {
        return doctorRepository.findAll()
                .stream().map(doctor -> modelMapper.map(doctor, DoctorServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDoctor(String id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid doctor identifier!"));
        doctor.getConsultations()
                .forEach(consultation -> consultation.setDoctor(null));
        doctorRepository.delete(doctor);
    }

    @Override
    public DoctorServiceModel getById(String id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid doctor identifier!"));
        return modelMapper.map(doctor, DoctorServiceModel.class);
    }

    @Override
    public DoctorServiceModel getByEmail(String email) {
        return doctorRepository.findByEmail(email)
                .map(doctor -> modelMapper.map(doctor, DoctorServiceModel.class))
                .orElse(null);
    }
}