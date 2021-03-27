package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.User;
import project.healthbox.domain.entities.enums.TitleEnum;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.error.ObjectNotFoundException;
import project.healthbox.events.register.RegisterEventPublisher;
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
    private final RegisterEventPublisher registerEventPublisher;

    //TODO INIT THE ROOT USER
    @Override
    public UserServiceModel register(UserServiceModel userServiceModel, TitleEnum title) {
        roleService.seedRolesInDb();

        if (userRepository.count() == 0 && doctorRepository.count() == 0) {
            userServiceModel.setAuthorities(roleService.getRolesForRootUser());
        } else {
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            if (title == TitleEnum.DOCTOR) {
                userServiceModel.getAuthorities().add(roleService.getByAuthority("ROLE_DOCTOR"));
            } else {
                userServiceModel.getAuthorities().add(roleService.getByAuthority("ROLE_USER"));
            }
        }

        User user = modelMapper.map(userServiceModel, User.class);
        Doctor doctor = modelMapper.map(userServiceModel, Doctor.class);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        doctor.setPassword(bCryptPasswordEncoder.encode(doctor.getPassword()));

        if (title == TitleEnum.DOCTOR && !doctorRepository.existsByEmail(doctor.getEmail())) {
            return modelMapper.map(doctorRepository.saveAndFlush(doctor), UserServiceModel.class);
        } else if (title == TitleEnum.PATIENT && !userRepository.existsByEmail(user.getEmail())) {
            UserServiceModel savedUser = modelMapper.map(userRepository.saveAndFlush(user), UserServiceModel.class);
            registerEventPublisher.publishUserRegisterEvent(savedUser);
            return savedUser;
        }
        return null;
    }

    @Override
    public UserServiceModel getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserServiceModel.class))
                .orElse(null);
    }

    @Override
    public UserServiceModel getById(String id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserServiceModel.class))
                .orElseThrow(() -> new ObjectNotFoundException("Invalid user identifier!"));
    }

    @Override
    public List<UserServiceModel> getAll() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid user identifier!"));

        userRepository.delete(user);
    }

    @Override
    public UserServiceModel makeAdmin(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid user identifier!"));

        UserServiceModel userServiceModel = modelMapper.map(user, UserServiceModel.class);
        userServiceModel.getAuthorities().clear();

        userServiceModel.getAuthorities().add(roleService.getByAuthority("ROLE_USER"));
        userServiceModel.getAuthorities().add(roleService.getByAuthority("ROLE_ADMIN"));

        return modelMapper.map(userRepository.saveAndFlush(modelMapper.map(userServiceModel, User.class)), UserServiceModel.class);
    }

    @Override
    public UserServiceModel makeUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid user identifier!"));

        UserServiceModel userServiceModel = modelMapper.map(user, UserServiceModel.class);
        userServiceModel.getAuthorities().clear();

        userServiceModel.getAuthorities().add(roleService.getByAuthority("ROLE_USER"));

        return modelMapper.map(userRepository.saveAndFlush(modelMapper.map(userServiceModel, User.class)), UserServiceModel.class);
    }
}