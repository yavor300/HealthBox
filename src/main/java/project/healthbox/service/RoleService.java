package project.healthbox.service;

import project.healthbox.domain.entities.Role;
import project.healthbox.domain.models.service.RoleServiceModel;

import java.util.Set;

public interface RoleService {
    void seedRoles();

    RoleServiceModel getByAuthority(String authority);

    Set<Role> getRolesForRootUser();
}
