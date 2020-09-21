package project.healthbox.service;

import project.healthbox.domain.models.binding.UserLoginBindingModel;
import project.healthbox.domain.models.service.ConsultationServiceModel;
import project.healthbox.domain.models.service.UserLoginServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;

public interface UserService {
    UserServiceModel register(UserServiceModel userServiceModel);

    UserLoginServiceModel login(UserLoginBindingModel userLoginBindingModel) throws Exception;

    UserServiceModel getById(String id);
}
