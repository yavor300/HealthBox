package project.healthbox.unit.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.Role;
import project.healthbox.domain.entities.User;
import project.healthbox.domain.entities.enums.TitleEnum;
import project.healthbox.domain.models.service.RoleServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.error.ObjectNotFoundException;
import project.healthbox.events.register.RegisterEventPublisher;
import project.healthbox.repostory.DoctorRepository;
import project.healthbox.repostory.UserRepository;
import project.healthbox.service.RoleService;
import project.healthbox.service.UserService;
import project.healthbox.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;


public class UserServiceTests {
    private static final String ID = "ID";
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String AUTHORITY_USER = "ROLE_USER";
    private static final String AUTHORITY_ADMIN = "ROLE_ADMIN";
    private static final String AUTHORITY_DOCTOR = "ROLE_DOCTOR";

    private UserService userService;
    private UserRepository mockUserRepository;
    private DoctorRepository mockDoctorRepository;
    private BCryptPasswordEncoder mockBCryptPasswordEncoder;
    private RoleService mockRoleService;
    private RegisterEventPublisher mockRegisterEventPublisher;

    private UserServiceModel userServiceModel;
    private User user;

    @Before
    public void init() {
        mockUserRepository = Mockito.mock(UserRepository.class);
        mockDoctorRepository = Mockito.mock(DoctorRepository.class);
        mockBCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        mockRoleService = Mockito.mock(RoleService.class);
        mockRegisterEventPublisher = Mockito.mock(RegisterEventPublisher.class);

        userService = new UserServiceImpl(new ModelMapper(), mockUserRepository, mockDoctorRepository, mockBCryptPasswordEncoder, mockRoleService, mockRegisterEventPublisher);

        userServiceModel = new UserServiceModel();
        userServiceModel.setId(ID);
        userServiceModel.setFirstName(FIRST_NAME);
        userServiceModel.setLastName(LAST_NAME);
        userServiceModel.setEmail(EMAIL);
        userServiceModel.setPassword(PASSWORD);

        user = new User();
        user.setId(ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setAuthorities(Set.of(new Role(AUTHORITY_USER)));

        Mockito.when(mockUserRepository.count())
                .thenReturn(1L);

        Mockito.when(mockDoctorRepository.count())
                .thenReturn(1L);

        Mockito.when(mockBCryptPasswordEncoder.encode(PASSWORD))
                .thenReturn(PASSWORD);
    }

    @Test
    public void register_Should_SaveUser() {
        Mockito.when(mockRoleService.getByAuthority(AUTHORITY_USER))
                .thenReturn(new RoleServiceModel() {{
                    setAuthority(AUTHORITY_USER);
                }});

        Mockito.when(mockUserRepository.existsByEmail(EMAIL))
                .thenReturn(false);

        Mockito.when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(user);

        UserServiceModel register = userService.register(userServiceModel, TitleEnum.PATIENT);

        Assert.assertEquals(user.getId(), register.getId());
        Assert.assertEquals(AUTHORITY_USER, register.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    public void register_Should_SaveDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(ID);
        doctor.setFirstName(FIRST_NAME);
        doctor.setLastName(LAST_NAME);
        doctor.setEmail(EMAIL);
        doctor.setPassword(PASSWORD);
        doctor.setAuthorities(Set.of(new Role(AUTHORITY_DOCTOR)));

        Mockito.when(mockRoleService.getByAuthority(AUTHORITY_DOCTOR))
                .thenReturn(new RoleServiceModel() {{
                    setAuthority(AUTHORITY_DOCTOR);
                }});

        Mockito.when(mockDoctorRepository.existsByEmail(EMAIL))
                .thenReturn(false);

        Mockito.when(mockDoctorRepository.saveAndFlush(any(Doctor.class)))
                .thenReturn(doctor);

        UserServiceModel register = userService.register(userServiceModel, TitleEnum.DOCTOR);

        Assert.assertEquals(doctor.getId(), register.getId());
        Assert.assertEquals(AUTHORITY_DOCTOR, register.getAuthorities().iterator().next().getAuthority());
    }


    @Test
    public void register_Should_ReturnNULL_IfUserExists() {
        Mockito.when(mockUserRepository.existsByEmail(EMAIL))
                .thenReturn(true);

        Mockito.when(mockRoleService.getByAuthority(AUTHORITY_USER))
                .thenReturn(new RoleServiceModel() {{
                    setAuthority(AUTHORITY_USER);
                }});

        UserServiceModel register = userService.register(userServiceModel, TitleEnum.PATIENT);

        Assert.assertNull(register);
    }

    @Test
    public void register_Should_ReturnNULL_IfDoctorExists() {
        Mockito.when(mockDoctorRepository.existsByEmail(EMAIL))
                .thenReturn(true);

        Mockito.when(mockRoleService.getByAuthority(AUTHORITY_DOCTOR))
                .thenReturn(new RoleServiceModel() {{
                    setAuthority(AUTHORITY_DOCTOR);
                }});

        UserServiceModel register = userService.register(userServiceModel, TitleEnum.DOCTOR);

        Assert.assertNull(register);
    }

    @Test
    public void getByEmail_Should_Return_CorrectUser() {
        Mockito.when(mockUserRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));

        UserServiceModel userServiceByEmail = userService.getByEmail(EMAIL);

        assertEquals(user.getId(), userServiceByEmail.getId());
        assertEquals(user.getFirstName(), userServiceByEmail.getFirstName());
        assertEquals(user.getLastName(), userServiceByEmail.getLastName());
        assertEquals(user.getEmail(), userServiceByEmail.getEmail());
    }

    @Test
    public void getByEmail_Should_Return_NULL() {
        Mockito.when(mockUserRepository.findByEmail(EMAIL))
                .thenReturn(Optional.empty());

        assertNull(userService.getByEmail(EMAIL));
    }

    @Test
    public void getById_Should_Return_CorrectUser() {
        Mockito.when(mockUserRepository.findById(ID))
                .thenReturn(Optional.of(user));

        UserServiceModel userServiceModel = userService.getById(ID);

        assertEquals(user.getId(), userServiceModel.getId());
        assertEquals(user.getFirstName(), userServiceModel.getFirstName());
        assertEquals(user.getLastName(), userServiceModel.getLastName());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getById_Should_ThrowAnException_If_UserDoesNotExist() {
        Mockito.when(mockDoctorRepository.findById(ID))
                .thenReturn(Optional.empty());

        userService.getById(ID);
    }

    @Test
    public void getAll_Should_Return_CollectionOfUsers() {
        Mockito.when(mockUserRepository.findAll())
                .thenReturn(List.of(user));

        List<UserServiceModel> result = userService.getAll();
        UserServiceModel userServiceModel = result.get(0);

        assertEquals(1, result.size());
        assertEquals(user.getId(), userServiceModel.getId());
    }

    @Test
    public void deleteUser_Should_ExecuteCorrectly() {
        Mockito.when(mockUserRepository.findById(ID))
                .thenReturn(Optional.of(user));

        userService.deleteUser(ID);

        Mockito.verify(mockUserRepository, times(1)).delete(user);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteUser_Should_ThrownAnException() {
        Mockito.when(mockUserRepository.findById(ID))
                .thenReturn(Optional.empty());

        userService.deleteUser(ID);
    }

    @Test
    public void makeAdmin_Should_UpdateTheRoles() {
        user.setAuthorities(Set.of(new Role(AUTHORITY_USER), new Role(AUTHORITY_ADMIN)));

        Mockito.when(mockUserRepository.findById(ID))
                .thenReturn(Optional.of(user));

        Mockito.when(mockRoleService.getByAuthority(AUTHORITY_USER))
                .thenReturn(new RoleServiceModel() {{
                    setAuthority(AUTHORITY_USER);
                }});

        Mockito.when(mockRoleService.getByAuthority(AUTHORITY_ADMIN))
                .thenReturn(new RoleServiceModel(){{
                    setAuthority(AUTHORITY_ADMIN);
                }});

        Mockito.when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(user);

        UserServiceModel result = userService.makeAdmin(ID);

        Assert.assertEquals(user.getId(), userServiceModel.getId());
        Assert.assertEquals(2, result.getAuthorities().size());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void makeAdmin_Should_ThrowAnException() {
        Mockito.when(mockUserRepository.findById(ID))
                .thenReturn(Optional.empty());

        userService.makeAdmin(ID);
    }

    @Test
    public void makeUser_Should_UpdateTheRoles() {
        Mockito.when(mockUserRepository.findById(ID))
                .thenReturn(Optional.of(user));

        Mockito.when(mockRoleService.getByAuthority(AUTHORITY_USER))
                .thenReturn(new RoleServiceModel() {{
                    setAuthority(AUTHORITY_USER);
                }});

        Mockito.when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(user);

        UserServiceModel result = userService.makeUser(ID);

        Assert.assertEquals(user.getId(), userServiceModel.getId());
        Assert.assertEquals(1, result.getAuthorities().size());
        Assert.assertEquals(AUTHORITY_USER, result.getAuthorities().iterator().next().getAuthority());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void makeUser_Should_ThrowAnException() {
        Mockito.when(mockUserRepository.findById(ID))
                .thenReturn(Optional.empty());

        userService.makeUser(ID);
    }
}