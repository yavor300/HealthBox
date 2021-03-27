package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Role;
import project.healthbox.domain.models.service.RoleServiceModel;
import project.healthbox.error.ObjectNotFoundException;
import project.healthbox.repostory.RoleRepository;
import project.healthbox.service.RoleService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public void seedRolesInDb() {
        if (roleRepository.count() == 0){
            roleRepository.saveAndFlush(new Role("ROLE_DOCTOR"));
            roleRepository.saveAndFlush(new Role("ROLE_USER"));
            roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
            roleRepository.saveAndFlush(new Role("ROLE_ROOT"));
        }
    }

    @Override
    public RoleServiceModel getByAuthority(String authority) {
        return roleRepository.findByAuthority(authority)
                .map(role -> modelMapper.map(role, RoleServiceModel.class))
                .orElseThrow(() -> new ObjectNotFoundException("Invalid role name!"));
    }

    @Override
    public Set<RoleServiceModel> getRolesForRootUser() {
        return roleRepository.findAll()
                .stream()
                .filter(role -> !role.getAuthority().equals("ROLE_DOCTOR"))
                .map(role -> modelMapper.map(role,RoleServiceModel.class))
                .collect(Collectors.toSet());
    }
}