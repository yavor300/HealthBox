package project.healthbox.service;

import project.healthbox.domain.entities.enums.TitleEnum;
import project.healthbox.domain.models.service.UserServiceModel;

import java.util.List;

public interface UserService {
    UserServiceModel register(UserServiceModel userServiceModel, TitleEnum title);

    UserServiceModel getByEmail(String email);

    UserServiceModel getById(String id);

    List<UserServiceModel> getAll();

    void deleteUser(String id);

    void makeAdmin(String id);

    void makeUser(String id);
}
