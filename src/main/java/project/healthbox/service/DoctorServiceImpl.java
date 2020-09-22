package project.healthbox.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.models.binding.DoctorUpdateBindingModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;
import project.healthbox.repostory.DoctorRepository;
import project.healthbox.repostory.SpecialtyRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    private final SpecialtyRepository specialtyRepository;
    private final CityService cityService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, ModelMapper modelMapper, SpecialtyRepository specialtyRepository, CityService cityService, CloudinaryService cloudinaryService) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
        this.specialtyRepository = specialtyRepository;
        this.cityService = cityService;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public DoctorServiceModel update(DoctorUpdateBindingModel doctorUpdateBindingModel) throws IOException {
        Doctor doctor = this.doctorRepository.getById(doctorUpdateBindingModel.getId());
        City city = this.modelMapper.map(this.cityService.getByName(doctorUpdateBindingModel.getLocation()), City.class);
        doctor.setLocation(city);
        doctor.setImageUrl(this.cloudinaryService.uploadImage(doctorUpdateBindingModel.getImage()));
        doctor.setBiography(doctorUpdateBindingModel.getBiography());
        doctor.setWorkHistory(doctorUpdateBindingModel.getWorkHistory());
        doctor.setEducation(doctorUpdateBindingModel.getEducation());
        doctor.setSpecialty(this.specialtyRepository.findByName(doctorUpdateBindingModel.getSpecialty()));
        return this.modelMapper.map( this.doctorRepository.saveAndFlush(doctor), DoctorServiceModel.class);
    }

    @Override
    public boolean isAccountCompleted(UserLoginServiceModel user) {
        Doctor doctor = this.doctorRepository.getById(user.getId());
        return doctor.getBiography() != null && doctor.getWorkHistory() != null &&
                doctor.getEducation() != null && doctor.getLocation() != null;

    }

    @Override
    public List<DoctorServiceModel> findAllByGivenCriteria(String specialtyId, String cityId, String doctorName) {
        if ((specialtyId == null || specialtyId.trim().isEmpty()) && (cityId == null || cityId.trim().isEmpty())) {
            return this.findAllByFirstNameAndLastName(doctorName.split("\\s+")[0], doctorName.split("\\s+")[1]);
        } else if ((specialtyId == null || specialtyId.trim().isEmpty()) && (doctorName == null || doctorName.trim().isEmpty())) {
            return this.findAllByLocationId(cityId);
        } else if ((cityId == null || cityId.trim().isEmpty()) && (doctorName == null || doctorName.trim().isEmpty())) {
            return this.findAllBySpecialtyId(specialtyId);
        } else if ((specialtyId == null || specialtyId.trim().isEmpty()) ) {
            return this.findAllByLocationIdAndFirstNameAndLastName(cityId, doctorName.split("\\s+")[0], doctorName.split("\\s+")[1]);
        } else if ((cityId == null || cityId.trim().isEmpty())) {
            return this.findAllBySpecialtyIdAndFirstNameAndLastName(specialtyId, doctorName.split("\\s+")[0], doctorName.split("\\s+")[1]);
        } else if ((doctorName == null || doctorName.trim().isEmpty())) {
            return this.findAllBySpecialtyIdAndLocationId(specialtyId, cityId);
        } else {
            return this.findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName(specialtyId, cityId, doctorName.split("\\s+")[0], doctorName.split("\\s+")[1]);
        }

//        if (model.getSpecialty() != null && !model.getSpecialty().trim().isEmpty() && model.getDoctorName().isEmpty() && model.getLocation().isEmpty()) {
//            return this.getAllBySpecialtyName(model.getSpecialty());
//        }
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
    public List<DoctorServiceModel> findAllByFirstNameAndLastName(String firstName, String lastName) {
        List<Doctor> allDoctorsByFirstNameAndLastName = this.doctorRepository.findAllByFirstNameAndLastName(firstName, lastName);

        if (allDoctorsByFirstNameAndLastName.isEmpty()) {
            return null;
        }

        return allDoctorsByFirstNameAndLastName.stream()
                .map(d -> this.modelMapper.map(d, DoctorServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorServiceModel> findAllByLocationId(String locationId) {
        List<Doctor> allDoctorsByLocationId = this.doctorRepository.findAllByLocationId(locationId);

        if (allDoctorsByLocationId.isEmpty()) {
            return null;
        }

        return allDoctorsByLocationId.stream()
                .map(d -> this.modelMapper.map(d, DoctorServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorServiceModel> findAllBySpecialtyId(String specialtyId) {
        List<Doctor> allDoctorsBySpecialtyId = this.doctorRepository.findAllBySpecialtyId(specialtyId);

        if (allDoctorsBySpecialtyId.isEmpty()) {
            return null;
        }

        return allDoctorsBySpecialtyId.stream()
                .map(d -> this.modelMapper.map(d, DoctorServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorServiceModel> findAllByLocationIdAndFirstNameAndLastName(String locationId, String firstName, String lastName) {
        List<Doctor> allByLocationIdAndFirstNameAndLastName = this.doctorRepository.findAllByLocationIdAndFirstNameAndLastName(locationId, firstName, lastName);

        if (allByLocationIdAndFirstNameAndLastName.isEmpty()) {
            return null;
        }

        return allByLocationIdAndFirstNameAndLastName.stream()
                .map(d -> this.modelMapper.map(d, DoctorServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorServiceModel> findAllBySpecialtyIdAndFirstNameAndLastName(String specialtyId, String firstName, String lastName) {
        List<Doctor> allBySpecialtyIdAndFirstNameAndLastName = this.doctorRepository.findAllBySpecialtyIdAndFirstNameAndLastName(specialtyId, firstName, lastName);

        if (allBySpecialtyIdAndFirstNameAndLastName.isEmpty()) {
            return null;
        }

        return allBySpecialtyIdAndFirstNameAndLastName.stream()
                .map(d -> this.modelMapper.map(d, DoctorServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorServiceModel> findAllBySpecialtyIdAndLocationId(String specialtyId, String locationId) {
        List<Doctor> allBySpecialtyIdAndLocationId = this.doctorRepository.findAllBySpecialtyIdAndLocationId(specialtyId, locationId);

        if (allBySpecialtyIdAndLocationId.isEmpty()) {
            return null;
        }

        return allBySpecialtyIdAndLocationId.stream()
                .map(d -> this.modelMapper.map(d, DoctorServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorServiceModel> findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName(String specialtyId, String locationId, String firstName, String lastName) {
        List<Doctor> allBySpecialtyIdAndLocationIdAndFirstNameAndLastName = this.doctorRepository.findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName(specialtyId, locationId, firstName, lastName);

        if (allBySpecialtyIdAndLocationIdAndFirstNameAndLastName.isEmpty()) {
            return null;
        }

        return allBySpecialtyIdAndLocationIdAndFirstNameAndLastName.stream()
                .map(d -> this.modelMapper.map(d, DoctorServiceModel.class))
                .collect(Collectors.toList());
    }
}
