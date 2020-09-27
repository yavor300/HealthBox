package project.healthbox.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import project.healthbox.domain.models.binding.UserLoginBindingModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    UserServiceModel register(UserServiceModel userServiceModel);

    UserLoginServiceModel login(UserLoginBindingModel userLoginBindingModel) throws Exception;

    UserServiceModel getByEmail(String email);

    UserServiceModel getById(String id);

    List<UserServiceModel> getAll();

    void deleteUser(String id);

    void makeAdmin(String id);

    void makeUser(String id);
}
