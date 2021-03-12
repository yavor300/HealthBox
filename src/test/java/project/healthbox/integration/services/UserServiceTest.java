package project.healthbox.integration.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import project.healthbox.domain.entities.City;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.User;
import project.healthbox.domain.models.service.CityServiceModel;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.repostory.CityRepository;
import project.healthbox.repostory.UserRepository;
import project.healthbox.service.CityService;
import project.healthbox.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserService service;

    @MockBean
    private UserRepository mockUserRepository;

    @Test
    public void getByEmailMethodShouldReturnCorrectEntity() {
        String email = "email";

        User user = new User();
        user.setEmail(email);

        Mockito.when(mockUserRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        UserServiceModel byEmail = service.getByEmail(email);

        assertEquals(user.getEmail(), byEmail.getEmail());
    }

    @Test
    public void getByEmailShouldReturnNull() {
        String email = "email";

        Mockito.when(mockUserRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        UserServiceModel result = service.getByEmail(email);

        assertNull(result);
    }

//    @Test
//    public void getByIdMethodShouldReturnCorrectEntity() {
//        String id = "id";
//
//        User user = new User();
//        user.setId(id);
//
//        Mockito.when(mockUserRepository.getById(id))
//                .thenReturn(user);
//
//        UserServiceModel result = service.getById(id);
//
//        assertEquals(user.getId(), result.getId());
//    }

    @Test
    public void getByAllMethodShouldReturnCollectionOfUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());

        Mockito.when(mockUserRepository.findAll())
                .thenReturn(users);

        List<UserServiceModel> result = service.getAll();

        assertEquals(users.size(), result.size());
    }
}
