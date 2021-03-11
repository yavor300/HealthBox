package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.error.DoctorNotFoundException;
import project.healthbox.repostory.DoctorRepository;
import project.healthbox.repostory.SpecialtyRepository;
import project.healthbox.service.CityService;
import project.healthbox.service.CloudinaryService;
import project.healthbox.service.DoctorService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    private final SpecialtyRepository specialtyRepository;
    private final CityService cityService;
    private final CloudinaryService cloudinaryService;


    @Override
    public DoctorServiceModel update(DoctorServiceModel doctorServiceModel) throws IOException {
        Doctor doctor = doctorRepository.findById(doctorServiceModel.getId())
                .orElseThrow(() -> new DoctorNotFoundException("Invalid doctor!"));

        City city = modelMapper.map(cityService.getByName(doctorServiceModel.getLocation().getName()), City.class);

        doctor.setLocation(city);
        doctor.setImageUrl(cloudinaryService.uploadImage(doctorServiceModel.getImage()));
        doctor.setBiography(doctorServiceModel.getBiography());
        doctor.setWorkHistory(doctorServiceModel.getWorkHistory());
        doctor.setEducation(doctorServiceModel.getEducation());
        //TODO USE SERVEICE INSTEAD
        doctor.setSpecialty(specialtyRepository.findByName(doctorServiceModel.getSpecialty().getName()));

        return modelMapper.map(doctorRepository.saveAndFlush(doctor), DoctorServiceModel.class);
    }

    @Override
    public List<DoctorServiceModel> getAll() {
        return this.doctorRepository.findAll()
                .stream().map(d -> this.modelMapper.map(d, DoctorServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDoctor(String id) {
        Doctor doctor = this.doctorRepository.getById(id);
        doctor.getConsultations()
                .forEach(consultation -> consultation.setDoctor(null));
        this.doctorRepository.delete(doctor);
    }

    @Override
    public DoctorServiceModel getById(String id) {
        Doctor doctor = this.doctorRepository.getById(id);
        return this.modelMapper.map(
                doctor,
                DoctorServiceModel.class
        );
    }

    @Override
    public DoctorServiceModel getByEmail(String email) {
        Doctor doctor = this.doctorRepository.findByEmail(email).orElse(null);

        if (doctor == null) {
            return null;
        }

        return this.modelMapper.map(doctor, DoctorServiceModel.class);
    }
}
