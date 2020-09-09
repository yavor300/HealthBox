package project.healthbox.service;

import project.healthbox.domain.models.binding.UserLoginBindingModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;

public interface UserService {
    UserServiceModel register(UserServiceModel userServiceModel) throws Exception;
    UserLoginServiceModel login(UserLoginBindingModel userLoginBindingModel) throws Exception;
}
