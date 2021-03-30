package project.healthbox.unit.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import project.healthbox.domain.entities.Role;
import project.healthbox.domain.models.service.RoleServiceModel;
import project.healthbox.error.ObjectNotFoundException;
import project.healthbox.repostory.RoleRepository;
import project.healthbox.service.RoleService;
import project.healthbox.service.impl.RoleServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;


public class RoleServiceTests {
    private static final String AUTHORITY = "AUTHORITY";
    private static final String ID = "UUID";

    private RoleRepository mockRoleRepository;
    private RoleService roleService;

    @Before
    public void init() {
        mockRoleRepository = Mockito.mock(RoleRepository.class);
        roleService = new RoleServiceImpl(mockRoleRepository, new ModelMapper());
    }

    @Test
    public void seedRoles_Should_SeedRolesCorrectly() {
        Role role = new Role();
        role.setAuthority(AUTHORITY);

        Mockito.when(mockRoleRepository.count())
                .thenReturn(0L);

        Mockito.when(mockRoleRepository.saveAndFlush(any(Role.class)))
                .thenReturn(role);

        roleService.seedRoles();

        Mockito.verify(mockRoleRepository, times(4)).saveAndFlush(any(Role.class));
    }

    @Test
    public void getByAuthority_Should_Return_CorrectDoctor() {
        Role role = new Role();
        role.setId(ID);
        role.setAuthority(AUTHORITY);

        Mockito.when(mockRoleRepository.findByAuthority(AUTHORITY))
                .thenReturn(Optional.of(role));

        RoleServiceModel roleServiceByAuthority = roleService.getByAuthority(AUTHORITY);

        assertEquals(role.getId(), roleServiceByAuthority.getId());
        assertEquals(role.getAuthority(), roleServiceByAuthority.getAuthority());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getByAuthority_Should_ThrowAnException_If_RoleDoesNotExist() {
        Mockito.when(mockRoleRepository.findByAuthority(AUTHORITY))
                .thenReturn(Optional.empty());

        roleService.getByAuthority(AUTHORITY);
    }

    @Test
    public void getRolesForRootUser_Should_SetRolesCorrectly() {
        Mockito.when(mockRoleRepository.findAll())
                .thenReturn(List.of(new Role() {{
                    setId(ID);
                    setAuthority(AUTHORITY);
                }}));

        Set<Role> rolesForRootUser = roleService.getRolesForRootUser();
        Role role = rolesForRootUser.iterator().next();

        assertEquals(1, rolesForRootUser.size());
        assertEquals(ID, role.getId());
        assertEquals(AUTHORITY, role.getAuthority());
    }
}