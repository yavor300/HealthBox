package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.User;
import project.healthbox.domain.entities.enums.TitleEnum;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.repostory.DoctorRepository;
import project.healthbox.repostory.UserRepository;
import project.healthbox.service.RoleService;
import project.healthbox.service.UserService;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;


    @Override
    public UserServiceModel register(UserServiceModel userServiceModel, TitleEnum title) {
        roleService.seedRolesInDb();

        if (userRepository.count() == 0 && doctorRepository.count() == 0) {
            userServiceModel.setAuthorities(roleService.setRolesForRootUser());
        } else {
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            if (title == TitleEnum.DOCTOR) {
                userServiceModel.getAuthorities().add(roleService.getByAuthority("ROLE_USER"));
            } else {
                userServiceModel.getAuthorities().add(roleService.getByAuthority("ROLE_DOCTOR"));
            }
        }

        User user = modelMapper.map(userServiceModel, User.class);
        Doctor doctor = modelMapper.map(userServiceModel, Doctor.class);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        doctor.setPassword(bCryptPasswordEncoder.encode(doctor.getPassword()));

        if (title == TitleEnum.DOCTOR && !doctorRepository.existsByEmail(doctor.getEmail())) {
            return modelMapper.map(doctorRepository.saveAndFlush(doctor), UserServiceModel.class);
        } else if (title == TitleEnum.PATIENT && !userRepository.existsByEmail(user.getEmail())) {
            return modelMapper.map(userRepository.saveAndFlush(user), UserServiceModel.class);
        }

        return null;
    }

    @Override
    public UserServiceModel getByEmail(String email) {
        User user = this.userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return null;
        }

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel getById(String id) {
        return this.modelMapper.map(this.userRepository.getById(id), UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> getAll() {
        return this.userRepository.findAll().stream()
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String id) {
        User user = this.userRepository.getById(id);
        this.userRepository.delete(user);
    }

    @Override
    public void makeAdmin(String id) {
        User user = this.userRepository.getById(id);

        UserServiceModel userServiceModel = this.modelMapper.map(user, UserServiceModel.class);
        userServiceModel.getAuthorities().clear();

        userServiceModel.getAuthorities().add(this.roleService.getByAuthority("ROLE_USER"));
        userServiceModel.getAuthorities().add(this.roleService.getByAuthority("ROLE_ADMIN"));

        this.userRepository.saveAndFlush(this.modelMapper.map(userServiceModel, User.class));
    }

    @Override
    public void makeUser(String id) {
        User user = this.userRepository.getById(id);

        UserServiceModel userServiceModel = this.modelMapper.map(user, UserServiceModel.class);
        userServiceModel.getAuthorities().clear();

        userServiceModel.getAuthorities().add(this.roleService.getByAuthority("ROLE_USER"));

        this.userRepository.saveAndFlush(this.modelMapper.map(userServiceModel, User.class));
    }
}