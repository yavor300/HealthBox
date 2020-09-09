package project.healthbox.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.User;
import project.healthbox.domain.models.binding.UserLoginBindingModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.repostory.DoctorRepository;
import project.healthbox.repostory.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository, DoctorRepository doctorRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) throws Exception {
        User user = this.modelMapper.map(userServiceModel, User.class);
        Doctor doctor = this.modelMapper.map(userServiceModel, Doctor.class);
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        doctor.setPassword(DigestUtils.sha256Hex(doctor.getPassword()));
        if (user.getTitle().equals("Doctor") && !this.userRepository.existsByEmail(doctor.getEmail())) {
            return this.modelMapper.map(this.doctorRepository.saveAndFlush(doctor), UserServiceModel.class);
        } else if (user.getTitle().equals("Patient") && !this.doctorRepository.existsByEmail(doctor.getEmail())) {
            return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
        } else {
            throw new Exception("Email is already taken!");
        }
    }

    @Override
    public UserLoginServiceModel login(UserLoginBindingModel userLoginBindingModel) throws Exception {
        String hashedPassword = DigestUtils.sha256Hex(userLoginBindingModel.getPassword());
        User user = this.userRepository.findByEmailAndPassword(userLoginBindingModel.getEmail(), hashedPassword).orElse(null);
        Doctor doctor = this.doctorRepository.findByEmailAndPassword(userLoginBindingModel.getEmail(), hashedPassword).orElse(null);

        if (user == null && doctor == null) {
            throw new Exception("Invalid User!");
        } else if (user == null) {
            return this.modelMapper.map(doctor, UserLoginServiceModel.class);
        } else {
            return this.modelMapper.map(user, UserLoginServiceModel.class);
        }
    }
}
